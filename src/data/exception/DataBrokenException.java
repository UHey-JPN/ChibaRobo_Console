package data.exception;

public class DataBrokenException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataBrokenException() {
		super("DataBase was Broken. Restart this system.");
	}

	public DataBrokenException(String message) {
		super("DataBase was Broken. Restart this system.:" + message);
	}

}
