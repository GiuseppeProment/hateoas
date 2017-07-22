package profile.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Image {
	@Id
	@GeneratedValue
	private Long id;
	@Column
	private String type;
	@ManyToOne
	private Product product;
	
	public Image() {
	}
	
	public Image(String type) {
		this.type = type;
	}
	public Image(String type, Product parent) {
		this( type );
		this.product = parent;
	}

	public Long getId() {
		return id;
	}
	public String getType() {
		return type;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setType(String type) {
		this.type = type;
	}
}
