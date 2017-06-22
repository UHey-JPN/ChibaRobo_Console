package data.communication;

public class ServerData {
	private String server_ip;
	private int console_port;
	private int db_port;
	private int kam_port;
	
	public ServerData(String server_ip, int console_port, int db_port, int kam_port) {
		this.server_ip = server_ip;
		this.console_port = console_port;
		this.db_port = db_port;
		this.kam_port = kam_port;
	}
	
	public String get_server_ip(){
		return server_ip;
	}
	public int get_console_port(){
		return console_port;
	}
	public int get_db_port(){
		return db_port;
	}
	public int get_kam_port(){
		return kam_port;
	}


	public boolean equals(ServerData data) {
		if( !server_ip.equals(data.get_server_ip()) ) return false;
		if( console_port != data.get_console_port() ) return false;
		if( db_port != data.get_db_port() ) return false;
		if( kam_port != data.get_kam_port() ) return false;
		return true;
	}

}
