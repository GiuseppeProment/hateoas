package profile.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import profile.component.CadastroRules;
import profile.component.LoginRules;
import profile.component.PerfilRules;
import profile.domain.Person;
import profile.dto.PersonDto;
import profile.dto.PersonDtoHelper;
import profile.repository.PersonRepository;

/**
 * My responsibility is to coordinate business process, (e.g. a orchestration stuff).
 * @author giuseppe.
 */
@Service
public class ProfileServiceImpl implements ProfileService {

	@Autowired private CadastroRules cadastroRules;
	@Autowired private PersonDtoHelper dtoHelper;
	@Autowired private LoginRules loginRules;
	@Autowired private PerfilRules perfilRules;
	@Autowired private PersonRepository repository;
	@Autowired private SecurityService security;
	
	@Override
	public PersonDto cadastro(PersonDto personDto) {
		PersonDto localDto = new PersonDto( personDto ); 
		Optional<Person> person = repository.findByEmail(personDto.getEmail());
		cadastroRules.check(person);
		String token = security.generateToken();
		iniatializeBeforeSave(localDto, token, personDto.getPassword());
		PersonDto updatedDto = saveAndReturnUpdated(localDto);
		updatedDto.setToken(token);
		return updatedDto;
	}

	@Override
	public PersonDto login(String email, String password) {
		Optional<Person> person = repository.findByEmail(email);
		loginRules.check(password, person);
		String token = security.generateToken();
		updateUponLogin(person.get(), token); 
		repository.saveAndFlush(person.get());
		PersonDto updatedDto = dtoHelper.dtoFromEntity(person.get());
		updatedDto.setToken(token);
		return updatedDto;
	}

	@Override
	public PersonDto perfil(String token, String userIdAsString ) {
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
