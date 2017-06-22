package data.communication;

import java.util.Calendar;

public class StateData {
	private int pac_id;
	private int game_id;
	private String mode;
	private String[] str_start_time = new String[7];
	private int[] score = new int[2];
	private String[] team_desc = new String[2];
	private String[] robot_desc = new String[4];
	private Calendar c_start_time;

	public StateData(
			int pac_id,
			int game_id,
			String mode,
			String[] start_time,
			int[] score,
			String[] team_desc,
			String[] robot_desc
	) {
		this.pac_id = pac_id;
		this.game_id = game_id;
		this.mode = new String(mode);
		for(int i = 0; i < str_start_time.length; i++) 
			this.str_start_time[i] = new String( start_time[i] );
		for(int i = 0; i < score.length; i++) 
			this.score[i] = score[i];
		for(int i = 0; i < team_desc.length; i++) 
			this.team_desc[i] = new String(team_desc[i]);
		for(int i = 0; i < robot_desc.length; i++) 
			this.robot_desc[i] = new String(robot_desc[i]);

		c_start_time = Calendar.getInstance();
		c_start_time.set(Calendar.YEAR, Integer.parseInt(str_start_time[0]));
		c_start_time.set(Calendar.MONTH, Integer.parseInt(str_start_time[1]));
		c_start_time.set(Calendar.DATE, Integer.parseInt(str_start_time[2]));
		c_start_time.set(Calendar.HOUR_OF_DAY, Integer.parseInt(str_start_time[3]));
		c_start_time.set(Calendar.MINUTE, Integer.parseInt(str_start_time[4]));
		c_start_time.set(Calendar.SECOND, Integer.parseInt(str_start_time[5]));
		c_start_time.set(Calendar.MILLISECOND, Integer.parseInt(str_start_time[6]));
	}

	public int get_pac_id(){
		return pac_id;
	}
	public int get_game_id(){
		return game_id;
	}
	public String get_mode(){
		return mode;
	}
	public String[] get_start_time_str(){
		return str_start_time;
	}
	public int[] get_score(){
		return score;
	}
	public String[] get_team_desc(){
		return team_desc;
	}
	public String[] get_robot_desc(){
		return robot_desc;
	}
	public Calendar get_c_start_time(){
		return c_start_time;
	}

}
