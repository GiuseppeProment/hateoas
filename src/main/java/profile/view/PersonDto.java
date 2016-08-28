package profile.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PersonDto {
	
	private UUID id;
	private String name;
	private String email;
	private String password;
	private Date created;
	private Date modified;
	private Date last_login;
	private String token;
	private List<PhoneDto> phones = new ArrayList<>();
	
	public PersonDto(PersonDto inDto) {
		setId(inDto.getId());
		setToken(inDto.getToken());
		setCreated(inDto.getCreated());
		setLast_login(inDto.getLast_login());
		setModified(inDto.getModified());
		setEmail(inDto.getEmail());
		setName(inDto.getName());
		setPassword(inDto.getPassword());
		setToken(inDto.getToken());
		inDto.getPhones()
			.stream()
			.forEach( e -> getPhones().add(new PhoneDto( e.getDdd(), e.getNumber() ) ) );
	}
	public PersonDto() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<PhoneDto> getPhones() {
		return phones;
	}
	public void setPhones(List<PhoneDto> phones) {
		this.phones = phones;
	}
	public UUID getId() {
		return id;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public Date getLast_login() {
		return last_login;
	}
	public void setLast_login(Date last_login) {
		this.last_login = last_login;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
}
