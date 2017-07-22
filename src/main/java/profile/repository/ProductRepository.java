package profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import profile.domain.Product;
	
public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findById( @Param("id") Long id);
	Product findFirstByName(@Param("name") String name );
}
