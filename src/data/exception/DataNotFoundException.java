package data.exception;

public class DataNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataNotFoundException() {
		super("Cannot find the data.");
	}

	public DataNotFoundException(String message) {
		super("Cannot find the data." + message);
	}

}
