package profile.repository;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import profile.domain.Image;
import profile.domain.Product;

@Projection(name="embeded", types= {Product.class,Image.class})
public interface ProductProjection {
	Long getId();
	String getDescription();
	String getName();
	List<Image> getImages();
	List<Product> getProducts();
}