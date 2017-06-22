package data.tournament;

import data.exception.GameNotEndedException;
import data.exception.IllegalTypeOfClassException;

class GameNode {
	public final static int TYPE_GAME = 1;
	public final static int TYPE_TEAM = 2;
	private GameNode[] game = new GameNode[2];
	private int type;
	private int number;
	private int winner = -1;
	private State state = State.NOT_YET;
	

	private GameNode(int type, int number, GameNode g0, GameNode g1){
		this.type = type;
		this.number = number;
		this.game[0] = g0;
		this.game[1] = g1;
	}
	
	GameNode(int team){
		this(TYPE_TEAM, team, (GameNode)null, (GameNode)null);
	}

	GameNode(int team, GameNode game){
		this(TYPE_GAME, 0, new GameNode(team), game);
	}

	GameNode(GameNode game, int team){
		this(TYPE_GAME, 0, game, new GameNode(team));
	}
	
	GameNode(GameNode g1, GameNode g2){
		this(TYPE_GAME, 0, g1, g2);
	}
	
	GameNode(){
		this(TYPE_GAME, 0, (GameNode)null, (GameNode)null);
	}

	boolean isGame(){
		if(this.type == GameNode.TYPE_GAME){
			return true;
		}
		return false;
	}
	boolean isEnded() throws IllegalTypeOfClassException{
		if(!this.isGame()) throw new IllegalTypeOfClassException("This instance type is not team but game.");
		return state.isEnded();
	}

	GameNode get_game(int id) throws ArrayIndexOutOfBoundsException{
		return game[id];
	}
	void set_game(int id, GameNode game) throws IllegalArgumentException{
		if(!(id == 0 | id == 1)) throw new IllegalArgumentException("id(" + id + ") is invalid");
		this.game[id] = game;
	}	
	
	int get_team() throws IllegalTypeOfClassException{
		if(this.isGame()) throw new IllegalTypeOfClassException("This instance type is not team but game.");
		return number;
	}
	int get_game_number() throws IllegalTypeOfClassException{
		if(!this.isGame()) throw new IllegalTypeOfClassException("This instance type is not game but team.");
		return number;
	}
	void set_number(int num){
		this.number = num;
	}
	
	void set_winner(int winner) throws IllegalArgumentException{
		if(winner == 0 || winner == 1 || winner == -1){
			this.winner = winner;
			state = State.ENDED;
		}else{
			throw new IllegalArgumentException("Arguments about winner is illegal.");
		}
	}
	GameNode get_winner() throws GameNotEndedException, IllegalTypeOfClassException{
		if(isGame()){
			if(this.isEnded()){
				return game[winner].get_winner();
			}else{
				throw new GameNotEndedException("id = " + number);
			}
		}else{
			return this;
		}
	}

	int get_winner_int() throws GameNotEndedException, IllegalTypeOfClassException{
		if(isGame()){
			if(this.isEnded()){
				return winner;
			}else{
				throw new GameNotEndedException("id = " + number);
			}
		}else{
			throw new IllegalTypeOfClassException();
		}
	}

}
