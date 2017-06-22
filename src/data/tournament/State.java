package data.tournament;

public enum State {
	NOT_YET(1),
	ENDED(2);
	
	private int val;
	
	State(int value){
		this.val = value;
	}
	
	boolean isEnded(){
		if(val == 1){
			return false;
		}else{
			return true;
		}
	}
}
