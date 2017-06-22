package data.exception;

public class GameNotEndedException extends Exception {

	private static final long serialVersionUID = 1L;

	public GameNotEndedException() {
		super("GameNotEndedException : this game is not ended");
	}

	public GameNotEndedException(String message) {
		super("GameNotEndedException : " + message);
	}

	public GameNotEndedException(int id) {
		super("GameNotEndedException : the game(id=" + id + ") is not ended");
	}


}
