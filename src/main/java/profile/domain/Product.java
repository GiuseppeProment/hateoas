package profile.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Product {
	@Id
	@GeneratedValue
	private Long id;
	@Column
	private String name;
	@Column
	private String description;
	@OneToMany(cascade = CascadeType.ALL, mappedBy="product", fetch=FetchType.LAZY)
	private List<Image> images = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.DETACH, mappedBy="parent", fetch=FetchType.LAZY)
	private List<Product> products = new ArrayList<>();
	
	@ManyToOne
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
