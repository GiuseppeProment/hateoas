package profile.domain;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

@Projection(name="images", types= {Product.class})
public interface ProductWithImagesProjection {
	Long getId();
	String getDescription();
	String getName();
	List<Image> getImages();
}