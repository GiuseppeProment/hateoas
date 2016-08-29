package profile.component;

import java.util.Optional;

import profile.domain.Person;
import profile.exception.DuplicateEmailException;

public interface CadastroRules {

	void check(Optional<Person> personFoundByEmail) throws DuplicateEmailException;

}