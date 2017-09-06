package communication.dataGetter;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

import data.communication.ServerData;
import window.logger.LogMessageAdapter;

public class TcpImageDataGetter implements Runnable {
	public static final String CRLF = "\r\n";
	private String name;
	private ImageDataGetterListener listener;
	private ServerData state;
	private LogMessageAdapter log_mes;
	
	public TcpImageDataGetter(String name, ImageDataGetterListener listener, ServerData state, LogMessageAdapter log_mes) {
		this.name = name;
		this.listener = listener;
		this.state = state;
		this.log_mes = log_mes;
		
		log_mes.log_println("data request (image)");
	}

	@Override
	public void run() {
		Socket soc = null;
		InputStream in = null;
		PrintWriter out = null;
		
		try {
			soc = new Socket(state.get_server_ip(), state.get_db_port());
			in = soc.getInputStream();
			out = new PrintWriter(soc.getOutputStream(), true);
			
			// set data type
			out.println( "image," + name );
			log_mes.log_println("  -> name = " + name);
			
			try {
				int size;
				long start_time;
				byte[] buf_header = new byte[128];
				byte[] buf_data;
				String header = "";
				String[] header_split;
				
				// 改行が1つ来るまで待つ
				for(int i = 0; ; i++){
					start_time = System.currentTimeMillis();
					while( in.available() < 1 ){				// Readで読み出せるなら読み出す
						if(System.currentTimeMillis() - start_time > 1000){
							// タイムアウト
							log_mes.log_println("Time Out.(while getting image \"" + name + "\")");
							listener.set_new_database(null);
							log_mes.log_println("header : " + header);
							return;
						}
						if(header.split(CRLF)[0].split(":")[0].equals("NAK")){
							// エラーを受信
							log_mes.log_println("NAK is sent from server. :" + header.split(CRLF)[0]);
							listener.set_new_database(null);
							return;
						}
					}
					buf_header[i] = (byte)in.read();
					header = new String(buf_header, 0, i+1);	// ヘッダー情報を保存
					if( (header+"line").split(CRLF).length == 2 ) break;// 改行で区切って、2つ以上
				}
				
				// ヘッダー情報を取り出し
				header_split = header.split(CRLF)[0].split(":");
				if( !header_split[0].equals("ACK") ){
					log_mes.log_println("NAK is sent from server. :" + header.split(CRLF)[0]);
					listener.set_new_database(null);
					return;
				}
				
				// ファイルサイズを読み出し
				size = Integer.parseInt(header_split[1]);
				buf_data = new byte[size];
				log_mes.log_println("  -> size = " + size);
				
				for(int i = 0; i < size; i++ ){
					int buf = in.read();
					if( buf == -1 ){
						listener.set_new_database(null);
						return;
					}
					buf_data[i] = (byte)buf;
				}
				
				// サーバーによるコネクション切断を待つ
				while( in.read() != -1 );
				
				listener.set_new_database(buf_data);

			} catch (IOException e) {
				log_mes.log_print(e);
				listener.set_new_database(null);
				return;
			}

		} catch (IOException e) {
			log_mes.log_print(e);
			listener.set_new_database(null);
			return;
		} finally {
			if( out != null ) out.close();
			try {
				if( soc != null ) soc.close();
			} catch (IOException e) {
				log_mes.log_print(e);
			}
		}
	}

}
