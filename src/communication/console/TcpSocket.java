package communication.console;

import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import communication.udp.UdpSocket;
import data.communication.FileDataManager;
import main.SettingManager;
import window.logger.LogMessageAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

public class TcpSocket
implements 
TcpConnectionListener, SetModeListener, SetScoreListener, 
SetWinnerListener, ClearDataListener, UploadDataListener{
	public static final String CRLF = "\r\n";
	public static final int TIMEOUT = 2000;
	
	private Socket soc;
	private UdpSocket udp;
	private BufferedReader in;
	private PrintWriter out;
	
	private SettingManager sm;
	
	private LogMessageAdapter log_mes;
	
	private FileDataManager fdm;

	public TcpSocket(UdpSocket udp, LogMessageAdapter log_mes, SettingManager sm, FileDataManager fdm) {
		this.sm = sm;
		this.udp = udp;
		this.log_mes = log_mes;
		this.fdm = fdm;
	}
	
	
	public synchronized boolean connect_to_server(){
		if( soc == null ){
			String ip = udp.get_server_data().get_server_ip();
			int port = udp.get_server_data().get_console_port();
			InetSocketAddress addr = new InetSocketAddress(ip, port);

			// connect to server
			try {
				soc = new Socket();
				soc.connect(addr, 200);
				soc.setSoTimeout(TIMEOUT);
				log_mes.log_println("connected to server("+ soc.getRemoteSocketAddress() +")");
			} catch (IOException e) {
				log_mes.log_print(e);
				this.shutdown_connection();
				return false;
			}
			
			// login process
			try {
				in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
				out = new PrintWriter(soc.getOutputStream(), true);
				
				// get key and make pass
				String[] str_key;
				try{
					str_key = in.readLine().split(",");
				}catch (SocketTimeoutException e){
					log_mes.log_println("time out(while wait to receive key).");
					this.shutdown_connection();
					return false;
				}
				
				if( str_key.length != 2 ){
					log_mes.log_println("key is illegal.");
					this.shutdown_connection();
					return false;
				}else{
					int[] key = { 0, 0 };
					for(int i = 0; i < key.length; i++){
						key[i] = Integer.parseInt(str_key[i]);
					}
					char[] c_pass = "chiba.robot.studio".toCharArray();
					for(int i = 0; i < c_pass.length; i++){
						c_pass[i] = (char)(((int)c_pass[i] * key[0] + key[1]) % 256 );
					}
					//out.print( (new String(c_pass)) + CRLF);
					//out.println( new String(c_pass) );
					out.println( "chiba.robot.studio" );
					log_mes.log_println("Send password.");
				}
				
				// receive ACK or NAK
				String ack;
				try{
					ack = in.readLine();
				}catch (SocketTimeoutException e){
					log_mes.log_println("time out(while wait to receive ACK or NAK).");
					this.shutdown_connection();
					return false;
				}
			
				if(ack.matches("Login : ACK")){
					log_mes.log_println("loging successed.");
					return true;
				}else{
					log_mes.log_println("loging failed.");
					this.shutdown_connection();
					return false;
				}

			} catch (IOException e) {
				log_mes.log_print(e);
				this.shutdown_connection();
				return false;
			}
		}else{
			log_mes.log_println("already login.");
			return false;
		}
	}
	
	public synchronized void shutdown_connection(){
		if( soc != null ){
			try {
				soc.close();
				soc = null;
				log_mes.log_println("socket was closed.");
			} catch (IOException e) {
				log_mes.log_print(e);
			}
		}else{
			log_mes.log_println("socket is already closed.");
		}
	}
	
	public synchronized void set_num_of_team(){
		if( soc != null ){
			// チーム数を取得
			out.println("get team_num");
			try {
				String l = in.readLine();
				if( sm.get_num_of_teams() == Integer.parseInt(l) ) return;
			} catch (SocketTimeoutException e){
				log_mes.log_print(e);
			} catch (IOException e) {
				log_mes.log_print(e);
			}
			
			out.println("set team_num " + sm.get_num_of_teams());
			try{
				String result = in.readLine();
				log_mes.log_println( result + " (get from server)" );
			} catch (SocketTimeoutException e){
				log_mes.log_print(e);
			} catch (IOException e) {
				log_mes.log_print(e);
			}
		}else{
			log_mes.log_println("socket is not opened.");
		}
	}
	
	// SetModeListener
	@Override
	public synchronized void set_mode(String mode){
		if( soc != null ){
			out.println("set mode " + mode);
			try{
				String result = in.readLine();
				log_mes.log_println( result + " (get from server)" );
			} catch (SocketTimeoutException e){
				log_mes.log_print(e);
			} catch (IOException e) {
				log_mes.log_print(e);
			}
		}else{
			log_mes.log_println("socket is not opened.");
		}
	}

	// SetScoreListener
	@Override
	public void set_score(int side0, int side1) {
		if( soc != null ){
			out.println("set score " + side0 + " " + side1 );
			try{
				String result = in.readLine();
				log_mes.log_println( result + " (get from server)" );
			} catch (SocketTimeoutException e){
				log_mes.log_print(e);
			} catch (IOException e) {
				log_mes.log_print(e);
			}
		}else{
			log_mes.log_println("socket is not opened.");
		}
	}

	// SetWinnerListener
	@Override
	public void set_winner(int side) {
		if( soc != null ){
			if( side == 0 | side == 1){
				out.println("set winner side" + side );
				try{
					String result = in.readLine();
					log_mes.log_println( result + " (get from server)" );
				} catch (SocketTimeoutException e){
					log_mes.log_print(e);
				} catch (IOException e) {
					log_mes.log_print(e);
				}
			}
		}else{
			log_mes.log_println("socket is not opened.");
		}
	}


	// TcpConnectionListener
	@Override
	public boolean connection() {
		boolean ret = this.connect_to_server();
		set_num_of_team();
		return ret;
	}
	@Override
	public void close() { this.shutdown_connection(); }

	// ClearDataListener
	@Override
	public void clear_data(int type) {
		if( soc != null ){
			if( type == ClearDataListener.TYPE_ROBOT){
				out.println("clear robot");
				try{
					String result = in.readLine();
					log_mes.log_println( result + " (get from server)" );
				} catch (SocketTimeoutException e){
					log_mes.log_print(e);
				} catch (IOException e) {
					log_mes.log_print(e);
				}
			}else if( type == ClearDataListener.TYPE_TEAM){
				out.println("clear team");
				try{
					String result = in.readLine();
					log_mes.log_println( result + " (get from server)" );
				} catch (SocketTimeoutException e){
					log_mes.log_print(e);
				} catch (IOException e) {
					log_mes.log_print(e);
				}
			}else{
				System.out.println("error in arguments");
			}
		}else{
			log_mes.log_println("socket is not opened.");
		}
		
	}


	@Override
	public void update_data(TYPE type) {
		if( soc != null ){
			if(type == UploadDataListener.TYPE.ROBOT){
				out.println("add robot");
				ArrayList<String> robot_list = fdm.get_robot();
				for(String robot : robot_list){
					if(robot.length() > 5){
						out.println(robot);
					}
				}
				out.println("EOF");
				
			}else if(type == UploadDataListener.TYPE.TEAM){
				out.println("add team");
				ArrayList<String> team_list = fdm.get_team();
				for(String team : team_list){
					if(team.length() > 4){
						out.println(team);
					}
				}
				out.println("EOF");
				
			}else if(type == UploadDataListener.TYPE.TOURNAMENT){
				int[] inte_team_list = fdm.get_tournament();
				String team_list = "" + inte_team_list[0];
				for(int i = 1; i < inte_team_list.length; i++){
					team_list += "," + inte_team_list[i];
				}
				out.println("set team_list " + team_list);
				
			}
			
			try{
				String result = in.readLine();
				log_mes.log_println( result + " (get from server)" );
			} catch (SocketTimeoutException e){
				log_mes.log_print(e);
			} catch (IOException e) {
				log_mes.log_print(e);
			}
		}else{
			log_mes.log_println("socket is not opened.");
		}
	}

}
