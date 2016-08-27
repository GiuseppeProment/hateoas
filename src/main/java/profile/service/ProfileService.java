package profile.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import profile.domain.Person;
import profile.domain.Phone;
import profile.repository.PersonRepository;
import profile.rest.exception.DuplicateEmailException;
import profile.rest.exception.InvalidSessionException;
import profile.rest.exception.InvalidTokenException;
import profile.rest.exception.UnauthorizedException;
import profile.rest.exception.UserNotFoundException;
import profile.view.PersonDto;
import profile.view.PhoneDto;

@Service
public class ProfileService {

	@Autowired PersonRepository repository;
	
	public PersonDto login(String email, String password) throws UserNotFoundException, UnauthorizedException {
		Optional<Person> person = repository.findByEmail(email);
		if ( ! person.isPresent() ) {
			throw new UserNotFoundException();
		};
		if ( ! person.get().getPassword().equals(DigestUtils.md5Hex(password))) {
			throw new UnauthorizedException();
		}
		return dtoFromEntity(person.get());
	}

	public PersonDto perfil(String token, String userIdAsString ) throws InvalidTokenException, InvalidSessionException {
		Optional<Person> person = repository.findByToken(token);
		if ( ! person.isPresent() ) {
			throw new InvalidTokenException();
		}
		if ( ! person.get().id.toString().equals(userIdAsString)) {
			throw new InvalidTokenException();
		}
		if ( (System.currentTimeMillis() - person.get().getLast_login().getTime())/1000/60 > 30 ) {
			throw new InvalidSessionException();
		}
		return dtoFromEntity(person.get());
	}

	public PersonDto record(PersonDto personDto) throws DuplicateEmailException {
		if (repository.findByEmail(personDto.getEmail()).isPresent() ) {
			throw new DuplicateEmailException();
		};
		Person entity = entityFromDto(personDto);
		entity.setCreated(new Date());
		entity.setModified(new Date());
		entity.setLast_login(new Date());
		entity.setToken( UUID.randomUUID().toString() );
		entity.setPassword(DigestUtils.md5Hex( personDto.getPassword() ) );
		return dtoFromEntity( repository.saveAndFlush(entity) );
	}

	private PersonDto dtoFromEntity(Person entity) {
		PersonDto dto = new PersonDto();
		dto.setId(entity.getId());
		dto.setToken(entity.getToken());
		dto.setCreated(entity.getCreated());
		dto.setLast_login(entity.getLast_login());
		dto.setModified(entity.getModified());
		dto.setEmail(entity.getEmail());
		dto.setName(entity.getName());
		dto.setPassword(entity.getPassword());
		entity.getPhones()
			.stream()
			.forEach( e -> dto.getPhones().add(new PhoneDto( e.getDdd(), e.getNumber() ) ) );
		return dto;
	}

	private Person entityFromDto(PersonDto dto) {
		Person entity = new Person();
		entity.setEmail(dto.getEmail());
		entity.setName(dto.getName());
		dto.getPhones()
			.stream()
			.forEach( d -> entity.getPhones().add(new Phone(d.getDdd(), d.getNumber())));
		return entity;
	}

}
