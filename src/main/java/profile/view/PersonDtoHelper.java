package profile.view;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
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

	public Person entityFromDto(PersonDto dto) {
		Person entity = new Person();
		entity.setEmail(dto.getEmail());
		entity.setName(dto.getName());
		entity.setCreated(new Date());
		entity.setModified(new Date());
		entity.setLast_login(new Date());
		entity.setToken( UUID.randomUUID().toString() );
		entity.setPassword(DigestUtils.md5Hex( dto.getPassword() ) );
		dto.getPhones()
			.stream()
			.forEach( d -> entity.getPhones().add(new Phone(d.getDdd(), d.getNumber())));
		return entity;
	}
}
