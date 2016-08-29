package profile.component;

import java.util.Optional;

import profile.domain.Person;
import profile.exception.UnauthorizedException;
import profile.exception.UserNotFoundException;

public interface LoginRules {

	void check(String password, Optional<Person> personFoundByEmail)
			throws UserNotFoundException, UnauthorizedException;

}