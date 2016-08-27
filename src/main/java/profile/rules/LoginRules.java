package profile.rules;

import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import profile.domain.Person;
import profile.rest.exception.UnauthorizedException;
import profile.rest.exception.UserNotFoundException;

@Component
public class LoginRules {
	public  void check(String password, Optional<Person> person)
			throws UserNotFoundException, UnauthorizedException {
		if ( ! person.isPresent() ) {
			throw new UserNotFoundException();
		};
		if ( ! person.get().getPassword().equals(DigestUtils.md5Hex(password))) {
			throw new UnauthorizedException();
		}
	}
}
