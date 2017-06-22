package data.tournament;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;

import communication.udp.ServerUpdateListener;
import communication.udp.TournamentUpdateListener;
import window.cardTournament.UpdateDatabaseListener;
import window.cardTournament.UpdateTourViewListener;
import window.main.LogMessageAdapter;

public class Tournament implements TournamentUpdateListener{
	private Game game_data;
	private int[] result;
	private UpdateTourViewListener update_view_listener;

	public Tournament(Executor ex, LogMessageAdapter log_mes) {
		game_data = new Game(ex, log_mes);
	}

	public void set_UpdateTourViewListener(UpdateTourViewListener l){
		update_view_listener = l;
	}
	
	// トーナメントの進行状況と試合結果を設定。
	public void set_result(int[] result){
		game_data.set_result(result);
		if(this.result != null){
			if(this.result.length == result.length){
				if( Arrays.equals(this.result, result) ){
					return;
				}
			}
		}
		if( update_view_listener != null ){
			this.result = result;
			update_view_listener.update_tour_view();
		}
	}
	
	// トーナメントの進行状況と試合結果を取得
	public int[] get_result(){
		return result;
	}
	
	// チームリストを取得
	public int[] get_team_list(){
		ArrayList<Integer> list = game_data.get_team_list();
		int[] ret = new int[list.size()];
		for(int i = 0; i < ret.length; i++){
			ret[i] = list.get(i);
		}
		return ret;
	}
	
	public int[] get_seed_info() {
		return game_data.get_seed_info();
	}
	 
	public int get_num_of_team() {
		return game_data.get_num_team();
	}

	public ServerUpdateListener get_game_as_SUL(){
		return game_data;
	}
	
	public UpdateDatabaseListener get_game_as_UDL(){
		return game_data;
	}

	@Override
	public void update_tournament_result(int[] result) {
		set_result(result);
	}

}
