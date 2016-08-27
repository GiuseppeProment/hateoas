package profile.rest.exception;

public class InvalidSessionException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidSessionException() {
		super("Sessão inválida");
	}

}
