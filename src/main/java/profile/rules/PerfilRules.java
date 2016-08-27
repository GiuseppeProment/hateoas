package profile.rules;

import java.util.Optional;

import org.springframework.stereotype.Component;

import profile.domain.Person;
import profile.rest.exception.InvalidSessionException;
import profile.rest.exception.InvalidTokenException;

@Component
public class PerfilRules {

	private static final int TOKEN_EXPIRATION_IN_MINUTES = 30;
	
	public  void check(String userIdAsString, Optional<Person> person)
			throws InvalidTokenException, InvalidSessionException {
		if ( ! person.isPresent() ) {
			throw new InvalidTokenException();
		}
		if ( ! person.get().id.toString().equals(userIdAsString)) {
			throw new InvalidTokenException();
		}
		if ( (System.currentTimeMillis() - person.get().getLast_login().getTime())/1000/60 > TOKEN_EXPIRATION_IN_MINUTES ) {
			throw new InvalidSessionException();
		}
	}
}
