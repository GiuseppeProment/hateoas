package profile.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import profile.domain.Person;
import profile.repository.PersonRepository;
import profile.rest.exception.DuplicateEmailException;
import profile.rest.exception.InvalidSessionException;
import profile.rest.exception.InvalidTokenException;
import profile.rest.exception.UnauthorizedException;
import profile.rest.exception.UserIdNotFoundException;
import profile.rest.exception.UserNotFoundException;
import profile.rules.CadastroRules;
import profile.rules.LoginRules;
import profile.rules.PerfilRules;
import profile.view.PersonDto;
import profile.view.PersonDtoHelper;

/**
 * My responsibility is to coordinate business process, (e.g. a orchestration stuff).
 * @author giuseppe.
 */
@Service
public class ProfileService {

	@Autowired private CadastroRules cadastroRules;
	@Autowired private PersonDtoHelper dtoHelper;
	@Autowired private LoginRules loginRules;
	@Autowired private PerfilRules perfilRules;
	@Autowired private PersonRepository repository;
	@Autowired private SecurityService security;
	
	public PersonDto cadastro(PersonDto personDto) throws DuplicateEmailException {
		PersonDto localDto = new PersonDto( personDto ); 
		Optional<Person> person = repository.findByEmail(personDto.getEmail());
		cadastroRules.check(person);
		String token = security.generateToken();
		iniatializeBeforeSave(localDto, token, personDto.getPassword());
		PersonDto updatedDto = saveAndReturnUpdated(localDto);
		updatedDto.setToken(token);
		return updatedDto;
	}

	public PersonDto login(String email, String password) throws UserNotFoundException, UnauthorizedException {
		Optional<Person> person = repository.findByEmail(email);
		loginRules.check(password, person);
		String token = security.generateToken();
		updateUponLogin(person.get(), token); 
		repository.saveAndFlush(person.get());
		PersonDto updatedDto = dtoHelper.dtoFromEntity(person.get());
		updatedDto.setToken(token);
		return updatedDto;
	}

	public PersonDto perfil(String token, String userIdAsString ) throws InvalidTokenException, InvalidSessionException, UserIdNotFoundException {
		Optional<Person> person = repository.findById( UUID.fromString(userIdAsString) );
		perfilRules.check(person, token);
		return dtoHelper.dtoFromEntity(person.get());
	}

	private void iniatializeBeforeSave(PersonDto dto, String token, String password) {
		dto.setCreated(new Date());
		dto.setModified(new Date());
		dto.setLast_login(new Date());
		dto.setPassword( security.digest( password ) );
		dto.setToken( security.digest( token ) );
	}

	private PersonDto saveAndReturnUpdated(PersonDto dto) {
		return dtoHelper.dtoFromEntity( 
				repository.saveAndFlush( 
						dtoHelper.entityFromDto( dto ) ) );
	}

	private void updateUponLogin(Person person, String token) {
		person.setLast_login(new Date());
		person.setToken(security.digest( token ) );
	}


}
