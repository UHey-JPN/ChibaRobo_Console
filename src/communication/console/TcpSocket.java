package communication.console;

import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import communication.udp.UdpSocket;
import data.communication.FileDataManager;
import data.image.Image;
import data.image.ImageList;
import data.image.ImageListComparater;
import data.robot.RoboList;
import main.SettingManager;
import window.logger.LogMessageAdapter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

public class TcpSocket
implements 
TcpConnectionListener, SetModeListener, SetScoreListener, 
SetWinnerListener, ClearDataListener, UploadDataListener{
	public static final String CRLF = "\r\n";
	public static final int TIMEOUT = 2000;
	public static final int MAX_FILE_SIZE = 1048576;
	
	private Socket soc;
	private UdpSocket udp;
	private BufferedReader in;
	private PrintWriter out;
	
	private SettingManager sm;
	
	private LogMessageAdapter log_mes;
	
	private FileDataManager fdm;
	
	private ProgressWindow p_win = new ProgressWindow();


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
				in = new BufferedReader(new InputStreamReader(soc.getInputStream(), "UTF-8"));
				OutputStreamWriter o_stream = new OutputStreamWriter(soc.getOutputStream(), "UTF-8");
				out = new PrintWriter(new BufferedWriter(o_stream), true);
				
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
					out.printf( "chiba.robot.studio"  + CRLF);
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
			out.printf("get team_num" + CRLF);
			try {
				String l = in.readLine();
				if( sm.get_num_of_teams() == Integer.parseInt(l) ) return;
			} catch (SocketTimeoutException e){
				log_mes.log_print(e);
			} catch (IOException e) {
				log_mes.log_print(e);
			}
			
			out.printf("set team_num " + sm.get_num_of_teams() + CRLF);
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
			out.printf("set mode " + mode + CRLF);
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
			out.printf("set score " + side0 + " " + side1  + CRLF);
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
				out.printf("set winner side" + side  + CRLF);
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
				out.printf("clear robot" + CRLF);
				try{
					String result = in.readLine();
					log_mes.log_println( result + " (get from server)" );
				} catch (SocketTimeoutException e){
					log_mes.log_print(e);
				} catch (IOException e) {
					log_mes.log_print(e);
				}
			}else if( type == ClearDataListener.TYPE_TEAM){
				out.printf("clear team" + CRLF);
				try{
					String result = in.readLine();
					log_mes.log_println( result + " (get from server)" );
				} catch (SocketTimeoutException e){
					log_mes.log_print(e);
				} catch (IOException e) {
					log_mes.log_print(e);
				}
			}else{
				System.out.printf("error in arguments" + CRLF);
			}
		}else{
			log_mes.log_println("socket is not opened.");
		}
		
	}


	@Override
	public void update_data(TYPE type) {
		if( soc != null ){
			if(type == UploadDataListener.TYPE.ROBOT){
				out.printf("add robot" + CRLF);
				ArrayList<String> robot_list = fdm.get_robot_array();
				for(String robot : robot_list){
					if(robot.length() > 5){
						out.printf(robot + CRLF);
					}
				}
				out.printf("EOF" + CRLF);
				
			}else if(type == UploadDataListener.TYPE.TEAM){
				out.printf("add team" + CRLF);
				ArrayList<String> team_list = fdm.get_team_array();
				for(String team : team_list){
					if(team.length() > 4){
						out.printf(team + CRLF);
					}
				}
				out.printf("EOF" + CRLF);
				
			}else if(type == UploadDataListener.TYPE.TOURNAMENT){
				int[] inte_team_list = fdm.get_tournament_array();
				String team_list = "" + inte_team_list[0];
				for(int i = 1; i < inte_team_list.length; i++){
					team_list += "," + inte_team_list[i];
				}
				out.printf("set team_list " + team_list + CRLF);
				
			}else if(type == UploadDataListener.TYPE.IMAGE){
				// robo_listを準備して、MD5リストを作成。
				// ローカルとサーバーで比較して、アップデートを実行
				RoboList robo_list = fdm.get_robolist();
				String img_floder = sm.get_image_folder_name();
				ImageList img_list = new ImageList(img_floder, log_mes, robo_list);
				String local  = img_list.get_md5_list();
				String remote = get_md5_list();
				ImageListComparater comp = new ImageListComparater(local, remote);
				
				// プログレスバー付きウィンドウを生成して、アップロードを開始する。
				// 同時にロガーにデータを残していく。
				int num_of_upload = comp.get_upload_list().size() + comp.get_update_list().size();
				p_win.reset(num_of_upload+1);
				
				log_mes.log_println("========= Update Image files =========");
				log_mes.log_println("[[ ok list ]]");
				for(String name : comp.get_ok_list()){
					log_mes.log_println("  * " + name);
				}

				log_mes.log_println("[[ upload list ]]");
				for(String name : comp.get_upload_list()){
					log_mes.log_println("  * " + name);
					try {
						send_img(img_list.get(name));
					} catch (FileNotFoundException e) {
						log_mes.log_print(e);
					}
					p_win.increase();
				}

				log_mes.log_println("[[ update list ]]");
				for(String name : comp.get_update_list()){
					log_mes.log_println("  * " + name);
					try {
						send_img(img_list.get(name));
					} catch (FileNotFoundException e) {
						log_mes.log_print(e);
					}
					p_win.increase();
				}

				log_mes.log_println("[[ delete list ]]");
				for(String name : comp.get_delete_list()){
					log_mes.log_println("  * " + name);
				}
				p_win.increase();
				p_win.closeable();
				
			}
			
			if(type != UploadDataListener.TYPE.IMAGE){
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
			// socket is not opened
			String message = "Try to update " + type + ". ";
			log_mes.log_println(message + "But, socket is not opened.");
		}
	}

	
	public synchronized boolean send_img(Image img) {
		if( img.get_name().matches("^.*[^\\p{ASCII}].*") ){
			// ASCII以外の文字を含む場合は除外
			log_mes.log_println("    -> ileagal file name (only ASCII characters).");
			return false;
		}
		if( img.size() > MAX_FILE_SIZE ){
			// 1MB以下のファイルに限定
			log_mes.log_println("    -> too large file size (smaller than 1MB).");
			return false;
		}
		try {
			String[] respo;
			try {
				soc.setSoTimeout(TIMEOUT);
				out.printf("image add " + img.get_name() + CRLF);	// 画像アップロードのコマンド
				respo = in.readLine().split(":");	// アップロード先の指示待ち
				
				if( !respo[0].equals("OK") ) return false;
				
				String[] soc_info = respo[1].split(",");
				String addr = soc_info[0];
				int port = Integer.parseInt(soc_info[1]);
				img.upload(new InetSocketAddress(addr, port));
			} catch(SocketTimeoutException e) {
				log_mes.log_println("Server do not send ip and port.(while sending image " + img.get_name() + ")");
				return false;
			}
			
			// サーバーでの処理結果を確認
			try {
				soc.setSoTimeout(TIMEOUT*10);
				if(in.readLine().matches("OK")){
					return true;
				}else{
					return false;
				}
			} catch(SocketTimeoutException e) {
				log_mes.log_println("Server do not send result of uplaod.(while sending image " + img.get_name() + ")");
				return false;
			}
		} catch (FileNotFoundException e) {
			log_mes.log_println("File(" + img.get_name() + ") is not exist.");
			return false;
		} catch (IOException e) {
			log_mes.log_print(e);
			return false;
		} finally {
			try {
				soc.setSoTimeout(TIMEOUT);
			} catch (SocketException e) {
				log_mes.log_print(e);
			}
		}
	}

	public String get_md5_list() {
		String ret = "";
		if( soc != null ){
			out.printf("image list" + CRLF);
			try{
				String result;
				while( !(result = in.readLine()).matches("") ){
					ret += result + CRLF;
				}
			} catch (SocketTimeoutException e){
				log_mes.log_print(e);
			} catch (IOException e) {
				log_mes.log_print(e);
			}
			return ret;
		}else{
			log_mes.log_println("socket is not opened.");
			return null;
		}
	}

	private class ProgressWindow extends JFrame implements ActionListener{
		private static final long serialVersionUID = 1L;
		private JProgressBar bar;
		private JLabel label;
		private JButton btn;
		private int cnt = 0;
		
		public ProgressWindow(){
			super("Now Uploading");
			this.setSize(400, 100);
			this.setLocationRelativeTo(null);
			this.setAlwaysOnTop(true);
			this.setResizable(false);
			
			JPanel p = new JPanel();
			p.setOpaque(true);
			p.setBackground(Color.white);
			p.setBorder(new EmptyBorder(0,10,0,10));
			
			bar = new JProgressBar(0, 100);
			
			btn = new JButton("OK");
			btn.addActionListener(this);
			
			label = new JLabel("0 / " + 100);
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setFont(new Font("", Font.PLAIN, 20));
			label.setBorder(new EmptyBorder(0,10,0,0));
			
			JPanel btn_panel = new JPanel();
			btn_panel.setOpaque(false);
			btn_panel.add(btn);

			p.setLayout(new BorderLayout());
			p.add(bar);
			p.add(label, BorderLayout.EAST);
			p.add(btn_panel, BorderLayout.SOUTH);
		
			getContentPane().add(p, BorderLayout.CENTER);
		}
		
		public void reset(int max){
			cnt = 0;
			bar.setMaximum(max);
			bar.setValue(cnt);
			btn.setEnabled(false);
			this.setTitle("Now Uploading");
			this.setVisible(true);
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}
		
		public void increase(){
			cnt++;
			bar.setValue(cnt);
			label.setText(cnt + " / " + bar.getMaximum());
		}
		
		public void closeable(){
			this.setTitle("Complete Uploading");
			this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			btn.setEnabled(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			this.setVisible(false);
		}
	}

}
