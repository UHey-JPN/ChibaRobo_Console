package data.exception;

public class AllGameIsEndedException extends Exception {

	private static final long serialVersionUID = 1L;

	public AllGameIsEndedException() {
		super("All of game is ended.");
	}

	public AllGameIsEndedException(String message) {
		super("All of game is ended:" + message);
	}

}
