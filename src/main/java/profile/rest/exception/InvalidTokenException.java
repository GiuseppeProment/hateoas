package profile.rest.exception;

public class InvalidTokenException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidTokenException() {
		super("Não autorizado");
	}

}
