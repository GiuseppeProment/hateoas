package profile.exception;

public class DuplicateEmailException extends Exception {
	private static final long serialVersionUID = 1L;

	public DuplicateEmailException() {
		super("E-mail já existente");
	}
}
