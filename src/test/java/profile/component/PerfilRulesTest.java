package profile.component;

import java.util.Date;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import profile.component.PerfilRulesImpl;
import profile.domain.Person;
import profile.exception.InvalidSessionException;
import profile.exception.InvalidTokenException;
import profile.exception.UserIdNotFoundException;
import profile.service.SecurityService;
import profile.service.SecurityServiceImpl;

public class PerfilRulesTest {

	private static final String INVALID_TOKEN = "invalid-token";
	private static final String VALID_TOKEN = "valid-token";
	private PerfilRulesImpl rules;
	private Person person;

	@Test
	public void checkIfTheLoginExpiredShouldPass() throws Exception {
		person.setLast_login(new Date());
		rules.checkIfTheLoginExpired(Optional.of(person));
	}

	@Test(expected=InvalidSessionException.class)
	public void checkIfTheLoginExpiredShouldNotPass() throws Exception {
		person.setLast_login(new Date(System.currentTimeMillis()-((rules.TOKEN_EXPIRATION_IN_MINUTES+1)*60*1000)));
		rules.checkIfTheLoginExpired(Optional.of(person));
	}

	@Test
	public void checkIfTheTokenIsValidShouldPassWithValidToken() throws Exception {
		rules.checkIfTheTokenIsValid(Optional.of(person), VALID_TOKEN);
	}

	@Test(expected=InvalidTokenException.class)
	public void checkIfTheTokenIsValidShouldNotPassWithInvalidToken() throws Exception {
		rules.checkIfTheTokenIsValid(Optional.of(person), INVALID_TOKEN);
	}

	@Test
	public void checkPersonFoundedShouldPass() throws Exception {
		rules.checkPersonFounded(Optional.of(person));
	}
	
	@Test(expected=UserIdNotFoundException.class)
	public void checkPersonFoundedShouldNotPass() throws Exception {
		rules.checkPersonFounded(Optional.empty());
	}
	
	@Before
	public void setup() {
		SecurityService sec = new SecurityServiceImpl();
		rules = new PerfilRulesImpl();
		person = new Person();
		person.setToken( sec.digest(VALID_TOKEN));
	}

}
