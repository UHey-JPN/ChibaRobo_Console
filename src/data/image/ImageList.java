package data.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import data.robot.RoboList;
import window.logger.LogMessageAdapter;

public class ImageList {
	public static final String CRLF = "\r\n";

	private List<Image> image_list;
	private File folder;
	private LogMessageAdapter log_mes;

	public ImageList(String img_folder, LogMessageAdapter log_mes) {
		image_list = Collections.synchronizedList(new ArrayList<Image>());
		this.folder = new File(img_folder);
		this.log_mes = log_mes;
		
		// フォルダーが存在しない場合、フォルダを生成
		if( !folder.exists() ){
			folder.mkdir();
		}
		
		// 画像用に指示された場所がフォルダーかどうかを確認する
		if( !folder.isDirectory() ){
			log_mes.log_println("Image Folder " + img_folder + "is not a directory.");
			System.exit(1);
		}
		
		this.update_list();
	}
	
	/**
	 * 画像を受信するメソッド。
	 * 結果出力用のアウトプットストリームとコマンドラインで受け取ったファイル名を受け取り、
	 * 画像受信を行う。その際、新しくソケットを生成して、データ用のコネクションを作る。
	 * 
	 * @param name - コマンドラインで受け取ったファイル名
	 * @param out - 実行結果を出力するためのストリーム
	 */
	public void receive_img(String name, PrintWriter out){
		Image img;
		ServerSocket listen = null;
		Socket soc = null;
		
		// 画像ファイル用のインスタンスを検索、なければ生成。
		try {
			img = this.get(name);
		} catch (FileNotFoundException e) {
			log_mes.log_println("create file " + name);
			try {
				img = new Image(new File(folder.getPath() + File.separator + name), log_mes);
			} catch (IOException e1) {
				out.println("err:illegal file name or another err");
				log_mes.log_print(e1);
				return;
			}
		}
		
		// ソケットを通知して、接続待ち
		try {
			// 待機用ソケットの生成
			listen = new ServerSocket(0, 1);
			listen.setSoTimeout(1000); // 1秒以内に接続が来なければタイムアウト
			
			// ソケットを通知して、接続待ち
			String addr = InetAddress.getLocalHost().getHostAddress();
			out.println("OK:" + addr + "," + listen.getLocalPort() );
			soc = listen.accept();
			
			// データの受信
			boolean result = img.receive(soc.getInputStream());
			
			// 受信プロセス結果の送信
			if(result){
				out.println("err:error has occurred during upload");
			}else{
				image_list.add(img);
				out.println("OK");
			}

		} catch (SocketTimeoutException e) {
			log_mes.log_println("time out(wait connection from client to upload image)");
			img.delete();
			
		} catch (IOException e) {
			log_mes.log_print(e);
			out.println("err:IOExceptino has occurred about socket");
			
	    } finally {
			try {
				if( soc != null) soc.close();
				if( listen != null ) listen.close();
			} catch (IOException e) {
				log_mes.log_print(e);
			}
		}
		
	}
	
	/**
	 * ファイル名を指定して、Imageクラスを取得するメソッド
	 * 
	 * @param name - 取得したいファイル名
	 * @return ファイルが存在し、インスタンスが生成されていれば、Imageクラスを返す。
	 * @throws FileNotFoundException ファイルが存在しない、または、インスタンスが生成されていないとき。
	 */
	public Image get(String name) throws FileNotFoundException{
		for(Image img : image_list){
			if( img.get_name().equals(name) ){
				return img;
			}
		}
		throw new FileNotFoundException(name + "is not found.");
	}
	
	/**
	 * フォルダー内の画像リストを更新する。
	 * たぶん、事あるごとに呼び出したほうが良い
	 */
	public void update_list(){
		image_list.clear();
		File[] list = this.folder.listFiles();
		Pattern pattern = Pattern.compile(".+\\..+"); // 隠しファイル以外を抽出
		for(File f : list){
			try {
				if( pattern.matcher(f.getName()).find() ){
					image_list.add(new Image(f, log_mes));
				}
			} catch (IOException e) {
				log_mes.log_print(e);
			}
		}
	}

	/**
	 * 取得したrobo_list内にあるファイル名から画像リストを作成
	 * 
	 */
	public void update_list(RoboList robo_list){
		image_list.clear();
		
		for(String s : robo_list.get_img_list()){
			File f = new File(folder.getName() + "/" + s);
			if( f.exists() ){
				try {
					image_list.add(new Image(f, log_mes));
				} catch (IOException e) {
					log_mes.log_print(e);
				}
			}else{
				log_mes.log_println("Image File \"" + s + "\" is not exist.");
			}
		}
	}
	
	
	/**
	 * 管理下のファイルの名前とハッシュ値（MD5）をリストとして返す。
	 * 
	 * @return
	 */
	public String get_md5_list(){
		String ret = "";
		for(Image img : image_list){
			try {
				ret += img.get_name() + "," + img.get_md5_str() + CRLF;
			} catch (FileNotFoundException e) {
				log_mes.log_print(e);
			}
		}
		
		return ret;
	}
}
