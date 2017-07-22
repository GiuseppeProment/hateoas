package profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import profile.domain.Image;
	
//@RestResource(exported = false)
public interface ImageRepository extends JpaRepository<Image, Long> {
	Image findById( @Param("id") Long id);
	Image findFirstByType(@Param("type") String type );
}
