package profile.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class Product {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private String description;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="product")
	@OrderBy("type ASC")
	private List<Image> images;
	
	@OneToMany( mappedBy="parent") 
	@OrderBy("name ASC")
	private List<Product> products;
	
	@ManyToOne(optional=true)
	private Product parent;

	public Product() {
	}
	public Product(String description) {
		this.description = description;
	}
	
	public Product(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}
	
	public Product(String name, String description, Product parent) {
		this( name, description );
		this.parent = parent;
	}
	public String getDescription() {
		return description;
	}
	
	public Long getId() {
		return id;
	}

	public List<Image> getImages() {
		return images;
	}

	public String getName() {
		return name;
	}
	
	public Product getParent() {
		return parent;
	}

	public List<Product> getProducts() {
		return products;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setImages(List<Image> images) {
		this.images = images;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setParent(Product parent) {
		this.parent = parent;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
