package profile.rules;

import java.util.Optional;

import org.springframework.stereotype.Component;

import profile.domain.Person;
import profile.rest.exception.InvalidSessionException;
import profile.rest.exception.InvalidTokenException;

@Component
public class PerfilRules {

	private static final int TOKEN_EXPIRATION_IN_MINUTES = 30;
	private static final int TOKEN_EXPIRATION_IN_MILISECONDS = TOKEN_EXPIRATION_IN_MINUTES*60*1000;
	
	public  void check(String userIdAsString, Optional<Person> personFoundByToken)
			throws InvalidTokenException, InvalidSessionException {
		if ( ! personFoundByToken.isPresent() ) {
			throw new InvalidTokenException();
		}
		if ( ! personFoundByToken.get().getId().toString().equals(userIdAsString)) {
			throw new InvalidTokenException();
		}
		if ( (System.currentTimeMillis() - personFoundByToken.get().getLast_login().getTime()) > TOKEN_EXPIRATION_IN_MILISECONDS ) {
			throw new InvalidSessionException();
		}
	}
}
