package profile.exception;

public class InvalidTokenException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidTokenException() {
		super("Não autorizado");
	}

}
