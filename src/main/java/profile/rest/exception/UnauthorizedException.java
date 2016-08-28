package profile.rest.exception;

public class UnauthorizedException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnauthorizedException() {
		super("Usuário e/ou senha inválidos");
	}

}
