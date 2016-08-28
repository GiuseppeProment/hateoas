package profile.view;

public class PhoneDto {

	public PhoneDto() {
		super();
	}

	public PhoneDto(String ddd, String number) {
		super();
		this.ddd = ddd;
		this.number = number;
	}

	private String number;
	private String ddd;
	
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

}
