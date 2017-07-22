package profile;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import profile.service.FixtureService;

@SpringBootApplication
public class ApplicationBootStrap {

	@Autowired
	private FixtureService fixture;
	
	public static void main(String[] args) {
		SpringApplication.run(ApplicationBootStrap.class, args);
	}

	@PostConstruct
	public void initData() {
		fixture.initializeDatabase();
	}
}
