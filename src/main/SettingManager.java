package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class SettingManager {
	private int num_of_teams = 21;
	private int[] point_list = {10,9,5,2,1};
	private String[] user_show_name = { "show0", "show1", "show2" };
	private String robots_file_name = "robots.csv";
	private String teams_file_name = "teams.csv";
	private String tnmt_file_name = "tnmt.csv";
	private String image_folder_name = "DB/img/";

	private File file = new File("./resource/setting.INI");

	public SettingManager(){
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			
			// read setting file
			String line;
			int line_num = 0;
			while( (line = br.readLine()) != null){
				line_num++;
				
				String[] line_split = line.split("=");
				if( line_split.length != 2 ) {
					report_syntax_err(line_num);
					continue;
				}
				
				if(line_split[0].equals("num_of_teams")){
					num_of_teams = Integer.parseInt(line_split[1]);
					
				}else if(line_split[0].equals("point_list")){
					String[] list = line_split[1].split(",");
					point_list = new int[list.length];
					for(int i = 0; i < list.length; i++){
						point_list[i] = Integer.parseInt(list[i]);
					}
					
				}else if(line_split[0].equals("user_show_name")){
					String[] list = line_split[1].split(",");
					if(list.length != 3){
						report_syntax_err(line_num);
						continue;
					}
					user_show_name = new String[list.length];
					for(int i = 0; i < list.length; i++){
						user_show_name[i] = list[i];
					}
					
				}else if(line_split[0].equals("robots_file_name")){
					robots_file_name = line_split[1];
					
				}else if(line_split[0].equals("teams_file_name")){
					teams_file_name = line_split[1];
					
				}else if(line_split[0].equals("tnmt_file_name")){
					tnmt_file_name = line_split[1];
					
				}else if(line_split[0].equals("image_folder_name")){
					image_folder_name = line_split[1];
					
				}else{
					report_syntax_err(line_num);
					continue;
				}
				
			}
		} catch (FileNotFoundException e) {
			save_data();
			report_create_file();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void report_syntax_err(int line) {
		JFrame f = new JFrame();
		JLabel label = new JLabel("Setting file has syntax error(in line " + line + ")");
		JOptionPane.showMessageDialog(f, label);
	}
	private void report_create_file() {
		JFrame f = new JFrame();
		JLabel label = new JLabel("Setting file is not exist. Create new setting file.");
		JOptionPane.showMessageDialog(f, label);
	}

	private void save_data(){
		PrintWriter bw = null;
		try {
			bw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			bw.println("num_of_teams=" + num_of_teams);
			
			bw.print("point_list=");
			for(int i = 0; i < point_list.length; i++){
				bw.print(point_list[i]);
				if( i != point_list.length-1) bw.print(",");
			}
			bw.println();
			
			bw.print("user_show_name=");
			for(int i = 0; i < user_show_name.length; i++){
				bw.print(user_show_name[i]);
				if( i != user_show_name.length-1) bw.print(",");
			}
			bw.println();
			
			bw.println("robots_file_name=" + robots_file_name);
	
			bw.println("teams_file_name=" + teams_file_name);
	
			bw.println("tnmt_file_name=" + tnmt_file_name);
			
			bw.println("image_folder_name=" + image_folder_name);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(bw != null) bw.close();
		}
	}
	
	// getter
	public int get_num_of_teams(){ return this.num_of_teams; }
	public int[] get_point_list(){ return this.point_list; }
	public String[] get_user_show_name(){ return this.user_show_name; }
	public String get_robots_file_name(){ return this.robots_file_name; }
	public String get_teams_file_name(){ return this.teams_file_name; }
	public String get_tnmt_file_name(){ return this.tnmt_file_name; }
	public String get_image_folder_name(){ return this.image_folder_name; }
	
	// setter
	public void set_num_of_teams(int num_of_teams){
		this.num_of_teams = num_of_teams;
		save_data();
	}
	public void set_point_list(int[] point_list){
		this.point_list = point_list;
		save_data();
	}
	public void set_user_show_name(String[] user_show_name){
		this.user_show_name = user_show_name;
		save_data();
	}
	public void set_robots_file_name(String robots_file_name){
		this.robots_file_name = robots_file_name;
		save_data();
	}
	public void set_teams_file_name(String teams_file_name){
		this.teams_file_name = teams_file_name;
		save_data();
	}
	public void set_tnmt_file_name(String tnmt_file_name){
		this.tnmt_file_name = tnmt_file_name;
		save_data();
	}
	public void set_image_folder_name(String image_folder_name){
		this.image_folder_name = image_folder_name;
		save_data();
	}
}
