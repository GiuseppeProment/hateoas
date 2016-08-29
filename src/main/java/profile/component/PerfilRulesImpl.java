package profile.component;

import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import profile.domain.Person;
import profile.exception.InvalidSessionException;
import profile.exception.InvalidTokenException;
import profile.exception.UserIdNotFoundException;

@Component
public class PerfilRulesImpl implements PerfilRules {

	static final int TOKEN_EXPIRATION_IN_MINUTES = 30;
	static final int TOKEN_EXPIRATION_IN_MILISECONDS = TOKEN_EXPIRATION_IN_MINUTES*60*1000;
	
	@Override
	public  void check(Optional<Person> person, String token)
			throws  InvalidTokenException, InvalidSessionException, UserIdNotFoundException {
		
		checkPersonFounded(person);
		checkIfTheTokenIsValid(person, token);
		checkIfTheLoginExpired(person);
	}

	void checkIfTheLoginExpired(Optional<Person> person) throws InvalidSessionException {
		if ( (System.currentTimeMillis() - person.get().getLast_login().getTime()) > TOKEN_EXPIRATION_IN_MILISECONDS ) {
			throw new InvalidSessionException();
		}
	}

	void checkIfTheTokenIsValid(Optional<Person> person, String token) throws InvalidTokenException {
		if ( ! person.get().getToken().equals( DigestUtils.md5Hex(token) )) {
			throw new InvalidTokenException();
		}
	}

	void checkPersonFounded(Optional<Person> person) throws UserIdNotFoundException {
		if ( ! person.isPresent() ) {
			throw new UserIdNotFoundException();
		}
	}
}
