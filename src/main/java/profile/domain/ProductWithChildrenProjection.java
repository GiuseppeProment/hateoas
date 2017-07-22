package profile.domain;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

@Projection(name="children", types= {Product.class})
public interface ProductWithChildrenProjection {
	Long getId();
	String getDescription();
	String getName();
	List<Product> getProducts();
}