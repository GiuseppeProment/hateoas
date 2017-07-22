package profile.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import profile.domain.Product;
	
public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findById( @Param("id") Long id);
	Product findFirstByName(@Param("name") String name );
//	@Query(value = "SELECT * FROM USERS WHERE EMAIL_ADDRESS = ?1", nativeQuery = true)
//	Product findSqlNative( @Param("id") Long id);
	@Query("select u from Product u where u.parent = :parent")
	List<Product> findByParent( @Param("parent") Product parent);
}
