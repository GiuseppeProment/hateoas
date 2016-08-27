package profile.rules;

import java.util.Optional;

import org.springframework.stereotype.Component;

import profile.domain.Person;
import profile.rest.exception.DuplicateEmailException;

@Component
public class CadastroRules {
	public  void check(Optional<Person> person) throws DuplicateEmailException {
		if (person.isPresent() ) {
			throw new DuplicateEmailException();
		};
	}

}
