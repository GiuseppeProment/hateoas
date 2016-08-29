package profile.component;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import profile.component.LoginRulesImpl;
import profile.domain.Person;
import profile.exception.UnauthorizedException;
import profile.exception.UserNotFoundException;
import profile.service.SecurityService;
import profile.service.SecurityServiceImpl;

public class LoginRulesTest {

	private LoginRulesImpl rules;
	private Person person;

	@Test
	public void checkIfThePasswordMatchShouldPass() throws Exception {
		rules.checkIfThePasswordMatch("secret", Optional.of(person));
	}
	@Test(expected=UnauthorizedException.class)
	public void checkIfThePasswordMatchShouldNotPass() throws Exception {
		rules.checkIfThePasswordMatch("wrong-password", Optional.of(person));
	}

	@Test(expected=UserNotFoundException.class)
	public void checkPersonFoundedShouldNotPass() throws Exception {
		rules.checkPersonFounded(Optional.empty());
	}

	@Test
	public void checkPersonFoundedShouldPass() throws Exception {
		rules.checkPersonFounded(Optional.of(person));
	}

	@Before
	public void setup() {
		SecurityService sec = new SecurityServiceImpl();
		rules = new LoginRulesImpl();
		person = new Person();
		person.setPassword(  sec.digest("secret"));
	}
}
