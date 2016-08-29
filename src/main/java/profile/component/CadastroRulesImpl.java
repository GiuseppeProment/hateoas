package profile.component;

import java.util.Optional;

import org.springframework.stereotype.Component;

import profile.domain.Person;
import profile.exception.DuplicateEmailException;

@Component
public class CadastroRulesImpl implements CadastroRules {
	@Override
	public  void check(Optional<Person> personFoundByEmail) throws DuplicateEmailException {
		checkPersonMustNotExist(personFoundByEmail);
	}

	void checkPersonMustNotExist(Optional<Person> personFoundByEmail) throws DuplicateEmailException {
		if ( personFoundByEmail.isPresent() ) {
			throw new DuplicateEmailException();
		};
	}

}
