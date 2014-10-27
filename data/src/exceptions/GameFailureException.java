package exceptions;

public class GameFailureException extends Exception {

	public GameFailureException(String message, Exception e) {
		super(message, e);
	}

	private static final long serialVersionUID = 1L;

}
