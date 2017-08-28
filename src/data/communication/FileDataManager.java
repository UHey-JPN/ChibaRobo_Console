package data.communication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import main.SettingManager;
import window.logger.LogMessageAdapter;

public class FileDataManager implements CheckIntegrityListener {
	private final String DB_FOLDER = "DB/";
	private final String IMG_FOLDER = DB_FOLDER+"img/";
	
	private SettingManager sm;
	private LogMessageAdapter log_mes;

	public FileDataManager(LogMessageAdapter log_mes, SettingManager sm) {
		this.log_mes = log_mes;
		this.sm = sm;
	}
	
	public ArrayList<String> get_robot(){
		ArrayList<String> ret = new ArrayList<String>();
		File file = new File(DB_FOLDER + sm.get_robots_file_name());
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"Shift_JIS"));
			
			// first line is item line.
			// skip first line.
			br.readLine();
			
			String line;
			while( (line = br.readLine()) != null ){
				ret.add(line);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return ret;
	}

	public ArrayList<String> get_team(){
		ArrayList<String> ret = new ArrayList<String>();
		File file = new File(DB_FOLDER + sm.get_teams_file_name());
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"Shift_JIS"));
			
			// first line is item line.
			// skip first line.
			br.readLine();
			
			String line;
			while( (line = br.readLine()) != null ){
				ret.add(line);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return ret;
	}
	
	public int[] get_tournament(){
		int[] ret = null;
		File file = new File(DB_FOLDER + sm.get_tnmt_file_name());
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"Shift_JIS"));
			
			// first line is item line.
			// skip first line.
			br.readLine();

			String[] line = br.readLine().split(",");
			
			ret = new int[line.length];
			for(int i = 0; i < ret.length; i++){
				ret[i] = Integer.parseInt(line[i]);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return ret;
	}
	
	@Override
	public boolean check_integrity(){
		boolean ret = true;
		
		log_mes.log_println("-------------- Confirm integrity of all files --------------");
		
		// --------------------------------------------------------
		// check tournament file
		log_mes.log_println("check tournament file.");
		int[] tournament = this.get_tournament();
		if( tournament != null ){
			if( tournament.length != sm.get_num_of_teams() ){
				log_mes.log_println("出場チーム数がSetting画面とtournament fileで一致していません。( "+ sm.get_num_of_teams() +" vs "+ tournament.length +" )");
				ret = false;
			}
			
			// check whether same number appear twice and more or not.
			for(int i = 0; i < tournament.length; i++){
				for(int j = i+1; j < tournament.length; j++){
					if( tournament[i] == tournament[j] ){
						log_mes.log_println("同じチームが２回以上指定されています。(in tournament file)");
						ret = false;
					}
				}
			}
		}
		
		
		// --------------------------------------------------------
		// check team file
		log_mes.log_println("check team file.");
		ArrayList<String> team = this.get_team();
		ArrayList<Integer> team_list = new ArrayList<Integer>();
		ArrayList<Integer> robot_list_teamfile = new ArrayList<Integer>();
		
		// check each team data
		// make robot list in team file
		for(String t : team){
			try{
				String[] split_team = t.split(",");
				if(split_team.length != 5){
					log_mes.log_println("チームデータの項目数は5である必要があります。( team id = " + split_team[0] + ")" );
					ret = false;
				}
				team_list.add( Integer.parseInt(split_team[0]) );
				robot_list_teamfile.add( Integer.parseInt(split_team[2]) );
				robot_list_teamfile.add( Integer.parseInt(split_team[3]) );
			}catch(ArrayIndexOutOfBoundsException e){
				// System.out.println("there is no data line in team file" );
			}
		}

		// check whether same number team appear twice and more in team file or not.
		for(int i = 0; i < team_list.size(); i++){
			for(int j = i+1; j < team_list.size(); j++){
				if( team_list.get(i) == team_list.get(j) ) {
					log_mes.log_println("同じチームが２回以上登録されています。(team id = " + team_list.get(i) + ")");
					ret = false;
				}
			}
		}

		// check whether there are all team in team file or not.
		TEAM_CHECK_LOOP:
			for(int i = 0; i < tournament.length; i++){
				for(Integer t : team_list){
					if(tournament[i] == t) continue TEAM_CHECK_LOOP;
				}
				log_mes.log_println("チームデータに存在しないチームがあります( team id = " + tournament[i] + ")" );
				ret = false;
			}
		
		
		// --------------------------------------------------------
		// check robot file
		log_mes.log_println("check robot file.");
		ArrayList<String> robot = this.get_robot();
		ArrayList<Integer> robot_list = new ArrayList<Integer>();
		ArrayList<RobotImg> image_list = new ArrayList<RobotImg>();
		
		// check each robot data
		// make image list in robot file
		for(String r : robot){
			try{
				String[] split_robot = r.split(",");
				if( split_robot.length != 6){
					log_mes.log_println("ロボットデータの項目数は5である必要があります。( robot id = " + split_robot[0] + ")" );
					ret = false;
				}
				robot_list.add( Integer.parseInt(split_robot[0]) );
				image_list.add( new RobotImg(split_robot[0] ,split_robot[4]) );
			}catch(ArrayIndexOutOfBoundsException e){
				// System.out.println("there is no data line in robot file" );
			}
		}

		// check whether same number team appear twice and more in team file or not.
		for(int i = 0; i < robot_list.size(); i++){
			for(int j = i+1; j < robot_list.size(); j++){
				if( robot_list.get(i) == robot_list.get(j) ) {
					log_mes.log_println("同じロボットが２回以上登録されています。(team id = " + robot_list.get(i) + ")");
					ret = false;
				}
			}
		}
		// check whether there are all robot in robot file or not.
		ROBOT_CHECK_LOOP:
			for(Integer r_in_team : robot_list_teamfile){
				for(Integer r : robot_list){
					if(r_in_team == r) continue ROBOT_CHECK_LOOP;
				}
				log_mes.log_println("ロボットデータに存在しないロボットがあります( robot id = " + r_in_team + ")" );
				ret = false;
			}
		
		// check image file
		for(RobotImg robot_img : image_list){
			File file = new File(IMG_FOLDER + robot_img.image);
			if( ! file.exists() ){
				log_mes.log_println("存在しない画像データがあります( robot id = " + robot_img.robot + ", file_name = " + robot_img.image + ")" );
				ret = false;
			}
		}
		
		return ret;
		
	}
	private class RobotImg{
		public int robot = 0;
		public String image = "";
		RobotImg(String robot, String img){
			this.robot = Integer.parseInt(robot);
			this.image = img;
		}
	}
	
}
