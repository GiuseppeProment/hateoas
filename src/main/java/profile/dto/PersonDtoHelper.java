package profile.dto;

import java.util.Date;

import org.springframework.stereotype.Component;

import profile.domain.Person;
import profile.domain.Phone;

/**
 * @author giuseppe
 * My responsibility is take care of entity to dto and inverse transformation.
 */
@Component
public class PersonDtoHelper {
	
	public PersonDto dtoFromEntity(Person entity) {
		PersonDto dto = new PersonDto();
		dto.setId(entity.getId());
		dto.setCreated(entity.getCreated());
		dto.setLast_login(entity.getLast_login());
		dto.setModified(entity.getModified());
		dto.setEmail(entity.getEmail());
		dto.setName(entity.getName());
		dto.setPassword(entity.getPassword());
		dto.setToken(entity.getToken());
		entity.getPhones()
			.stream()
			.forEach( e -> dto.getPhones().add(new PhoneDto( e.getDdd(), e.getNumber() ) ) );
		return dto;
	}

	public Person entityFromDto(PersonDto dto) {
		Person entity = new Person();
		entity.setEmail(dto.getEmail());
		entity.setName(dto.getName());
		entity.setCreated(dto.getCreated());
		entity.setModified(dto.getModified());
		entity.setLast_login(dto.getLast_login());
		entity.setToken( dto.getToken() );
		entity.setPassword( dto.getPassword() );
		dto.getPhones()
			.stream()
			.forEach( d -> entity.getPhones().add(new Phone(d.getDdd(), d.getNumber())));
		return entity;
	}
}
