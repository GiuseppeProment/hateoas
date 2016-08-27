package profile.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import profile.domain.Person;
	
public interface PersonRepository extends JpaRepository<Person, Long> {
	Optional<Person> findByEmail(String email);
	Optional<Person> findByToken(String token);
}
