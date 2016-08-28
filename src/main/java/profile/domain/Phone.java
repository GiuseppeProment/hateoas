package profile.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Phone {

	public Phone() {
		super();
	}

	public Phone(String ddd, String number) {
		super();
		this.ddd = ddd;
		this.number = number;
	}

	@Column
	private String number;
	
	@Column
	private String ddd;
	
	@Id
	@GeneratedValue
	public UUID id;
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public UUID getId() {
		return id;
	}

}
