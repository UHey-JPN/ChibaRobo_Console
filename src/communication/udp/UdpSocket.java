package communication.udp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.Executor;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import data.communication.ServerData;
import data.communication.StateData;
import window.logger.LogMessageAdapter;

public class UdpSocket implements Runnable{
	static final String LINE_SEPARATOR_PATTERN =  "\r\n|[\n\r\u2028\u2029\u0085]";

	public static final int PORT_NUM = 58239;
	public static final int BUF_SIZE = 2048;
	private DatagramSocket soc;
	private LogMessageAdapter log_mes;
	
	private StateData show_state;
	private ServerData server_data;
	private volatile boolean server_socket_receive = false;
	
	// listener list
	ArrayList<StateUpdateListener> list_state_listener = new ArrayList<StateUpdateListener>();
	ArrayList<ServerUpdateListener> list_server_listener = new ArrayList<ServerUpdateListener>();
	ArrayList<TournamentUpdateListener> list_tournament_listener = new ArrayList<TournamentUpdateListener>();
	

	public UdpSocket(Executor ex, LogMessageAdapter log_mes) {
		// hold value
		this.log_mes = log_mes;
		
		// open socket
		try {
			soc = new DatagramSocket(PORT_NUM);
			log_mes.log_println("Open UDP Port(" + soc.getLocalPort() + ")");
		} catch (SocketException e) {
			e.printStackTrace();
			JFrame f = new JFrame();
			JLabel label = new JLabel("UDP Port 58239 is already used.");
			JOptionPane.showMessageDialog(f, label);
			System.exit(1);
		}
		
		ex.execute(this);
		
		// while( !server_socket_receive );
	}
	
	///////////////////////////////////////////////////////////////////
	// getter of method in this class
	///////////////////////////////////////////////////////////////////
	public int get_local_port(){
		return soc.getLocalPort();
	}
	
	public boolean is_open(){
		return soc.isConnected();
	}
	
	public String get_ip(){
		return soc.getLocalAddress().getHostAddress();
	}

	
	///////////////////////////////////////////////////////////////////
	// about State Packet
	///////////////////////////////////////////////////////////////////
	public void add_StateUpdateListener(StateUpdateListener listener){
		this.list_state_listener.add(listener);
	}
	private void call_state_listener(){
		for(StateUpdateListener l : list_state_listener){
			l.state_update( show_state );
		}
	}
	private void process_state_packet(String[] pac_data){
		if( pac_data.length < 12){
			log_mes.log_println("packet length is illegal(in show state packet process):" + pac_data.length);
			for(int i=0; i < pac_data.length; i++){
				log_mes.log_println("\t" + pac_data[i]);
			}
			return;
		}
		
		int pac_id = Integer.parseInt(pac_data[1]);
		int game_id = Integer.parseInt(pac_data[2]);
		String mode = pac_data[3];
		String[] start_time = pac_data[4].split(",");
		if( start_time.length != 7 ){
			log_mes.log_println("start time is illegal(in show state packet process).");
			return;
		}
		String[] str_score = pac_data[5].split(",");
		if( str_score.length != 2){
			log_mes.log_println("score length is illegal(in show state packet process).");
			return;
		};
		int[] score = {Integer.parseInt(str_score[0]), Integer.parseInt(str_score[1])};
		String[] team_desc = {pac_data[6], pac_data[9]};
		String[] robot_desc = {pac_data[7], pac_data[8], pac_data[10], pac_data[11]};
		
		if( show_state != null ){
			if( show_state.get_pac_id() == pac_id ) return;
		}
		
		show_state = new StateData( 
				pac_id, game_id, mode, start_time, score, team_desc, robot_desc );
		
		call_state_listener();
		
		// send UDP Keep Alive data
		if(server_socket_receive == false){
			log_mes.log_println("socket number is not informed(in show state packet process).");
			return;
		}
		
		
	}
	

	///////////////////////////////////////////////////////////////////
	// about Tournament Packet
	///////////////////////////////////////////////////////////////////
	public void add_TournamentUpdateListener(TournamentUpdateListener listener){
		this.list_tournament_listener.add(listener);
	}
	private void call_tournament_listener(int[] result){
		for(TournamentUpdateListener l : list_tournament_listener){
			l.update_tournament_result(result);
		}
	}
	private void process_tournament_packet(String[] pac_data){
		String[] str_result = pac_data[1].split(",");
		int[] result = new int[str_result.length];
		for(int i = 0; i < result.length; i++){
			result[i] = Integer.parseInt(str_result[i]);
		}
		
		call_tournament_listener(result);
		
	}
	
	
	///////////////////////////////////////////////////////////////////
	// about Server Packet
	///////////////////////////////////////////////////////////////////
	public void add_ServerUpdateListener(ServerUpdateListener listener){
		list_server_listener.add(listener);
	}
	public void call_server_data(){
		for(ServerUpdateListener l : list_server_listener){
			l.server_update( server_data );
		}
	}
	private void process_server_packet(String[] pac_data, String ip){
		String server_ip;
		int console_port = -1;
		int db_port = -1;
		int kam_port = -1;
		
		if( pac_data.length != 6 ) return; 
		
		if(pac_data[1].equals(ip)){
			server_ip = pac_data[1];
		}else{
			log_mes.log_println("server may be illegal.");
			server_ip = ip;
		}
		
		for(int i = 1; i < 5; i++){
			if( pac_data[i].indexOf("console") != -1 ){
				console_port = Integer.parseInt(pac_data[i].split(",")[1]);
			}
			if( pac_data[i].indexOf("database_port") != -1 ){
				db_port = Integer.parseInt(pac_data[i].split(",")[1]);
			}
			if( pac_data[i].indexOf("kam_port") != -1 ){
				kam_port = Integer.parseInt(pac_data[i].split(",")[1]);
			}
		}
		
		// check if the data is changed
		ServerData new_data = new ServerData(server_ip, console_port, db_port, kam_port);
		if( server_data == null ){
			server_data = new_data;
		}else{
			if( ! server_data.equals(new_data) ){
				server_data = new_data;
			}
		}
		
		call_server_data();
		
		server_socket_receive = true;
	}
	public ServerData get_server_data(){
		return server_data;
	}
	

	///////////////////////////////////////////////////////////////////
	// UDP Socket Process
	///////////////////////////////////////////////////////////////////
	@Override
	public void run() {
		while(true){
			byte[] buf = new byte[BUF_SIZE];
			DatagramPacket pac = new DatagramPacket(buf, buf.length);
			String str_packet;
			
			//----------------------------------------
			// UDPソケットへのデータ到着待ち
			try {
				soc.receive(pac);
			} catch (IOException e) {
				// エラーのスタックトレースを表示
				log_mes.log_print(e);
			}
			
			//----------------------------------------
			// 文字列をUTF-8でデコード
			try {
				str_packet = new String(buf, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return;
			}
			
			//----------------------------------------
			// 改行コードで分割
			String[] pac_split = str_packet.split( LINE_SEPARATOR_PATTERN );
			
			//----------------------------------------
			// 受信パケットの分類とそれに伴う処理
			if( pac_split[0].equals("show") ){
				// log_mes.log_println("Get Packet - show");
				process_state_packet(pac_split);
			}else if( pac_split[0].equals("tournament") ){
				// log_mes.log_println("Get Packet - tournament");
				process_tournament_packet(pac_split);
			}else if( pac_split[0].equals("server") ){
				// log_mes.log_println("Get Packet - server");
				process_server_packet(pac_split, pac.getAddress().getHostAddress());
			}else{
				log_mes.log_println("Get Packet(unknow packet)\n" + str_packet);
			}
		}
	}

}
