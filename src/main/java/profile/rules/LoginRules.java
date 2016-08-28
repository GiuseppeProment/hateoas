package profile.rules;

import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import profile.domain.Person;
import profile.rest.exception.UnauthorizedException;
import profile.rest.exception.UserNotFoundException;

@Component
public class LoginRules {
	public  void check(String password, Optional<Person> personFoundByEmail)
			throws UserNotFoundException, UnauthorizedException {
		
		checkPersonFounded(personFoundByEmail);
		checkIfThePasswordMatch(password, personFoundByEmail);
	}

	void checkIfThePasswordMatch(String password, Optional<Person> personFoundByEmail)
			throws UnauthorizedException {
		if ( ! personFoundByEmail.get().getPassword().equals(DigestUtils.md5Hex(password))) {
			throw new UnauthorizedException();
		}
	}

	void checkPersonFounded(Optional<Person> personFoundByEmail) throws UserNotFoundException {
		if ( ! personFoundByEmail.isPresent() ) {
			throw new UserNotFoundException();
		};
	}
}
