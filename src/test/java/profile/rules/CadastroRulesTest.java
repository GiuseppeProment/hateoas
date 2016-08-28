package profile.rules;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import profile.domain.Person;
import profile.rest.exception.DuplicateEmailException;

public class CadastroRulesTest {

	private CadastroRules rules;

	@Test(expected=DuplicateEmailException.class)
	public void checkPersonMustNotExistShouldNotPass() throws Exception {
		rules.checkPersonMustNotExist(Optional.of(new Person()));
	}

	@Test
	public void checkPersonMustNotExistShouldPass() throws Exception {
		rules.checkPersonMustNotExist(Optional.empty());
	}

	@Before
	public void setup() {
		rules = new CadastroRules();
	}
}
