package data.exception;

public class IllegalTypeOfClassException extends Exception {

	private static final long serialVersionUID = 1L;

	public IllegalTypeOfClassException() {
		// TODO Auto-generated constructor stub
	}

	public IllegalTypeOfClassException(String message) {
		super("class type is illegal:" + message);
		// TODO Auto-generated constructor stub
	}
}
