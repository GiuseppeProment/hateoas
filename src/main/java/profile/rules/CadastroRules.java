package profile.rules;

import java.util.Optional;

import org.springframework.stereotype.Component;

import profile.domain.Person;
import profile.rest.exception.DuplicateEmailException;

@Component
public class CadastroRules {
	public  void check(Optional<Person> personFoundByEmail) throws DuplicateEmailException {
		if (personFoundByEmail.isPresent() ) {
			throw new DuplicateEmailException();
		};
	}

}
