package profile.rules;

import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import profile.domain.Person;
import profile.rest.exception.InvalidSessionException;
import profile.rest.exception.InvalidTokenException;
import profile.rest.exception.UserIdNotFoundException;

@Component
public class PerfilRules {

	private static final int TOKEN_EXPIRATION_IN_MINUTES = 30;
	private static final int TOKEN_EXPIRATION_IN_MILISECONDS = TOKEN_EXPIRATION_IN_MINUTES*60*1000;
	
	public  void check(Optional<Person> person, String token)
			throws  InvalidTokenException, InvalidSessionException, UserIdNotFoundException {
		
		if ( ! person.isPresent() ) {
			throw new UserIdNotFoundException();
		}
		
		if ( ! person.get().getToken().equals( DigestUtils.md5Hex(token) )) {
			throw new InvalidTokenException();
		}
		
		if ( (System.currentTimeMillis() - person.get().getLast_login().getTime()) > TOKEN_EXPIRATION_IN_MILISECONDS ) {
			throw new InvalidSessionException();
		}
	}
}
