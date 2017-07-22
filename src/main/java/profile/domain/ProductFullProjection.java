package profile.domain;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

@Projection(name="full", types= {Product.class})
public interface ProductFullProjection {
	Long getId();
	String getDescription();
	String getName();
	List<Image> getImages();
	List<Product> getProducts();
}