package profile.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import profile.domain.Person;
import profile.repository.PersonRepository;
import profile.rest.exception.DuplicateEmailException;
import profile.rest.exception.InvalidSessionException;
import profile.rest.exception.InvalidTokenException;
import profile.rest.exception.UnauthorizedException;
import profile.rest.exception.UserNotFoundException;
import profile.rules.CadastroRules;
import profile.rules.LoginRules;
import profile.rules.PerfilRules;
import profile.view.PersonDto;
import profile.view.PersonDtoHelper;

/**
 * @author giuseppe
 * My responsibility is to coordinate business process, (e.g. a orchestration stuff).
 */
@Service
public class ProfileService {

	@Autowired private PersonDtoHelper dtoHelper;
	@Autowired private PersonRepository repository;
	@Autowired private CadastroRules cadastroRules;
	@Autowired private LoginRules loginRules;
	@Autowired private PerfilRules perfilRules;
	
	public PersonDto cadastro(PersonDto personDto) throws DuplicateEmailException {
		Optional<Person> person = repository.findByEmail(personDto.getEmail());
		cadastroRules.check(person);
		Person entity = dtoHelper.entityFromDto(personDto);
		return dtoHelper.dtoFromEntity( repository.saveAndFlush(entity) );
	}

	public PersonDto login(String email, String password) throws UserNotFoundException, UnauthorizedException {
		Optional<Person> person = repository.findByEmail(email);
		loginRules.check(password, person);
		return dtoHelper.dtoFromEntity(person.get());
	}

	public PersonDto perfil(String token, String userIdAsString ) throws InvalidTokenException, InvalidSessionException {
		Optional<Person> person = repository.findByToken(token);
		perfilRules.check(userIdAsString, person);
		return dtoHelper.dtoFromEntity(person.get());
	}


}
