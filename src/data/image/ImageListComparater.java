package data.image;

import java.util.ArrayList;

public class ImageListComparater {
	static final String LINE_SEPARATOR_PATTERN =  "\r\n|[\n\r\u2028\u2029\u0085]";
	
	private ArrayList<String> ok = new ArrayList<String>();
	private ArrayList<String> upload = new ArrayList<String>();
	private ArrayList<String> update = new ArrayList<String>();
	private ArrayList<String> delete = new ArrayList<String>();

	public ImageListComparater(String local_str, String remote_str) {
		ArrayList<String[]> local  = str_to_list(local_str);
		ArrayList<String[]> remote = str_to_list(remote_str);
		
		// ローカル側の同一名のファイルを1つにする。複数ファイルのなかで最後の1つのみを残す。
		{
			ArrayList<String[]> delete_number = new ArrayList<String[]>();
			for(int i = 0; i < local.size(); i++){
				for(int j = i+1; j < local.size(); j++){
					if(local.get(i)[0].equals(local.get(j)[0])){
						delete_number.add(local.get(j));
					}
				}
			}
			
			//複数のファイルのなかで最後の1つのみを残す。
			for(String[] d : delete_number) local.remove(d);
		}
		
		for(String[] local_property : local){
			String[] img = null;
			for(String[] remote_property : remote){
				// 同じ名前を抽出
				if( local_property[0].equals(remote_property[0]) ){
					if( local_property[1].equals(remote_property[1]) ){
						// 同じハッシュ値
						ok.add(local_property[0]);
					}else{
						// 異なるハッシュ値
						update.add(local_property[0]);
					}
					img = remote_property;
					break;
				}
			}
			if(img != null){
				// 同一名ファイルが見つかった場合
				remote.remove(img);
			}else{
				// 同一名ファイルが見つからなかった場合
				upload.add(local_property[0]);
			}
		}
		
		// remoteに残っているファイルは不要なファイル
		for(String[] property : remote){
			delete.add(property[0]);
		}
	}
	
	public ArrayList<String> get_ok_list(){
		return ok;
	}
	public ArrayList<String> get_upload_list(){
		return upload;
	}
	public ArrayList<String> get_update_list(){
		return update;
	}
	public ArrayList<String> get_delete_list(){
		return delete;
	}

	static ArrayList<String[]> str_to_list(String str){
		ArrayList<String[]> ret = new ArrayList<String[]>();
		
		String array[] = str.split(LINE_SEPARATOR_PATTERN);
		for(String s : array){
			ret.add(s.split(","));
		}
		
		return ret;
	}


}
