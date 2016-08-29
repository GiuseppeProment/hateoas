package profile.dto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import profile.domain.Person;
import profile.domain.Phone;
import profile.dto.PersonDto;
import profile.dto.PersonDtoHelper;
import profile.dto.PhoneDto;

public class PersonDtoHelperTest extends PersonDtoHelper {

	private PersonDto dto;
	private Person entity;
	private PersonDtoHelper helper;

	@Test
	public void dtoFromEntityShouldCopyCreated() {
		assertThat(helper.dtoFromEntity(entity).getCreated(), equalTo(entity.getCreated()));
	}

	@Test
	public void dtoFromEntityShouldCopyEmail() {
		assertThat(helper.dtoFromEntity(entity).getEmail(), equalTo(entity.getEmail()));
	}

	@Test
	public void dtoFromEntityShouldCopyId() {
		assertThat(helper.dtoFromEntity(entity).getId(), equalTo(entity.getId()));
	}

	@Test
	public void dtoFromEntityShouldCopyLast_login() {
		assertThat(helper.dtoFromEntity(entity).getLast_login(), equalTo(entity.getLast_login()));
	}

	@Test
	public void dtoFromEntityShouldCopyModified() {
		assertThat(helper.dtoFromEntity(entity).getModified(), equalTo(entity.getModified()));
	}

	@Test
	public void dtoFromEntityShouldCopyName() {
		assertThat(helper.dtoFromEntity(entity).getName(), equalTo(entity.getName()));
	}

	@Test
	public void dtoFromEntityShouldCopyPassword() {
		assertThat(helper.dtoFromEntity(entity).getPassword(), equalTo(entity.getPassword()));
	}

	@Test
	public void dtoFromEntityShouldCopyPhones() {
		assertThat(helper.dtoFromEntity(entity).getPhones().size(), equalTo(entity.getPhones().size()));
	}

	@Test
	public void dtoFromEntityShouldCopyToken() {
		assertThat(helper.dtoFromEntity(entity).getToken(), equalTo(entity.getToken()));
	}

	@Test
	public void entityFromDtoShouldCopyCreated() {
		assertThat(helper.entityFromDto(dto).getCreated(), equalTo(dto.getCreated()));
	}

	@Test
	public void entityFromDtoShouldCopyEmail() {
		assertThat(helper.entityFromDto(dto).getEmail(), equalTo(dto.getEmail()));
	}

	@Test
	public void entityFromDtoShouldCopyLast_login() {
		assertThat(helper.entityFromDto(dto).getLast_login(), equalTo(dto.getLast_login()));
	}

	@Test
	public void entityFromDtoShouldCopyModified() {
		assertThat(helper.entityFromDto(dto).getModified(), equalTo(dto.getModified()));
	}

	@Test
	public void entityFromDtoShouldCopyName() {
		assertThat(helper.entityFromDto(dto).getName(), equalTo(dto.getName()));
	}

	@Test
	public void entityFromDtoShouldCopyPassword() {
		assertThat(helper.entityFromDto(dto).getPassword(), equalTo(dto.getPassword()));
	}

	@Test
	public void entityFromDtoShouldCopyPhones() {
		assertThat(helper.entityFromDto(dto).getPhones().size(), equalTo(dto.getPhones().size()));
	}

	@Test
	public void entityFromDtoShouldCopyToken() {
		assertThat(helper.entityFromDto(dto).getToken(), equalTo(dto.getToken()));
	}

	@Before
	public void setup() {
		helper = new PersonDtoHelper();
		entity = wellKnowEntity();
		dto = wellKnowDto();
	}

	private PersonDto wellKnowDto() {
		ArrayList<PhoneDto> phones = new ArrayList<PhoneDto>();
		phones.add(new PhoneDto("11", "55890444"));
		PersonDto person = new PersonDto();
		person.setName("Jose");
		person.setEmail("jose@gmail.com");
		person.setPassword("secret");
		person.setId(UUID.randomUUID());
		person.setToken("the-token");
		person.setCreated(new Date());
		person.setModified(new Date());
		person.setLast_login(new Date());
		person.setPhones(phones);
		return person;
	}

	private Person wellKnowEntity() {
		ArrayList<Phone> phones = new ArrayList<Phone>();
		phones.add(new Phone("43", "7777777"));
		Person person = new Person();
		person.setName("Caroline");
		person.setEmail("caroline@libero.it");
		person.setPassword("another-secret");
		person.setId(UUID.randomUUID());
		person.setToken("the-caroline-token");
		person.setCreated(new Date());
		person.setModified(new Date());
		person.setLast_login(new Date());
		person.setPhones(phones);
		return person;
	}

}
