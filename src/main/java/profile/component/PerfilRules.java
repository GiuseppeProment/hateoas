package profile.component;

import java.util.Optional;

import profile.domain.Person;
import profile.exception.InvalidSessionException;
import profile.exception.InvalidTokenException;
import profile.exception.UserIdNotFoundException;

public interface PerfilRules {

	void check(Optional<Person> person, String token)
			throws InvalidTokenException, InvalidSessionException, UserIdNotFoundException;

}