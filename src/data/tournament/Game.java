package data.tournament;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.Executor;

import communication.dataGetter.DatabaseGetterListener;
import communication.dataGetter.TcpDataGetter;
import communication.udp.ServerUpdateListener;
import data.communication.ServerData;
import data.exception.IllegalTypeOfClassException;
import window.cardTournament.UpdateDatabaseListener;
import window.logger.LogMessageAdapter;

public class Game implements ServerUpdateListener, DatabaseGetterListener, UpdateDatabaseListener {
	public final static int TYPE_TEAM = 0;
	public final static int TYPE_GAME = 1;
	private GameNode root;
	private int num_team;

	private Executor ex;
	private ServerData state = null;
	private LogMessageAdapter log_mes;
	
	private boolean initilize = false;
	
	// シードの位置を保存
	private int[] seed_info;
	
	
	Game(Executor ex, LogMessageAdapter log_mes) {
		this.num_team = 2;
		this.ex = ex;
		this.log_mes = log_mes;
		
		root = new GameNode(new GameNode(0), new GameNode(1));
	}
	
	public int get_num_team(){
		return num_team;
	}
	
	// 試合結果をint型配列で設定するメソッド
	// -1     : 試合が行われていない
	// 0 or 1 : 勝者が0または1
	synchronized void set_result(int[] result){
		if( result.length != num_team-1 ) return;
		
		Queue<GameNode> q = new ArrayDeque<GameNode>();
		q.add(root);
		while(!q.isEmpty()){
			GameNode g = q.remove();
			if(g.isGame()){
				// キューに子ノードを追加
				q.add(g.get_game(1));
				q.add(g.get_game(0));
				// 試合番号と試合結果の取得
				try {
					int num = g.get_game_number();
					g.set_winner(result[num]);
				} catch (IllegalTypeOfClassException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void update_tournament(){
		log_mes.log_println("----------------- update tournament -----------------");
		ex.execute( new TcpDataGetter(TcpDataGetter.TYPE.TOUR, this, this.state, log_mes) );
	}

	
	@Override
	public void update_database() {
		update_tournament();
	}

	@Override
	public void set_new_database(String data) {
		TournamentXmlParser xml_process = new TournamentXmlParser(data);	
		root = xml_process.get_root();
		try {
			num_team = root.get_game_number() + 2;
		} catch (IllegalTypeOfClassException e) {
			num_team = 0;
			e.printStackTrace();
		}
		// トーナメントの最終段の形状を計算
		calc_seed_info();
	}

	@Override
	public void server_update(ServerData state) {
		this.state = state;
		if( initilize == false ){
			update_tournament();
			initilize = true;
		}
	}

	public synchronized ArrayList<Integer> get_team_list() {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		Deque<GameNode> s = new ArrayDeque<GameNode>();
		s.addFirst(root);
		while(!s.isEmpty()){
			GameNode g = s.removeFirst();
			if(g.isGame()){
				s.addFirst(g.get_game(1));
				s.addFirst(g.get_game(0));
			}else{
				try {
					int id = g.get_team();
					ret.add(id);
				} catch (IllegalTypeOfClassException e) {
				}
			}
		}
		return ret;
	}

	private void calc_seed_info(){
		// ラウンド数を計算
		int round;
		for(round = 0; Math.pow(2, round) < num_team; round++);

		// 配列に保存するネスト位置
		int target_nest = round-1;
		
		// 配列初期化
		seed_info = new int[(int)Math.pow(2, target_nest)];
		
		Queue<GameNode> q = new ArrayDeque<GameNode>();
		
		// ターゲットのネスト位置までキューの出し入れをする
		q.add(root);
		for(int nest = 0; nest < target_nest; nest++){
			for(int i = 0; i < Math.pow(2, nest); i++ ){
				GameNode g = q.remove();
				if(g.isGame()){
					// キューに子ノードを追加
					q.add(g.get_game(0));
					q.add(g.get_game(1));
				}
			}
		}
		
		for(int i = 0; !q.isEmpty(); i++){
			GameNode g = q.remove();
			if( g.isGame() ){
				seed_info[i] = Game.TYPE_GAME;
			}else{
				seed_info[i] = Game.TYPE_TEAM;
			}
		}
	}
	
	int[] get_seed_info() {
		return seed_info;
	}


}
