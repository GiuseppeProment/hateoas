package profile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import profile.domain.Image;
import profile.repository.ImageRepository;
import profile.service.FixtureService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageTest {

	@Autowired
	private WebApplicationContext ctx;

	@Autowired
	private ObjectMapper mapper;

	private MockMvc mvc;

	@Autowired
	private ImageRepository repository;

	@Autowired
	private FixtureService fixture;
	
	private byte[] createImageAsJson(String type) throws Exception {
		return mapper.writeValueAsBytes(new Image(type));
	}

	private Long getIdFromLocation(String location) {
		return Long.valueOf(location.substring(location.lastIndexOf('/')+1));
	}
	
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
		fixture.initializeDatabase();
	}
	
	@Test
	public void shouldCreateImage() throws Exception {
		String location = 
			// @formatter:off
			mvc
				.perform( 
						post("/images")
							.content( createImageAsJson("BlueTooth Image") )
							.contentType(MediaType.APPLICATION_JSON_UTF8)
						)
				.andExpect( status().isCreated() )
				.andExpect( header().string("Location", startsWith("http://localhost/images/")))
				.andReturn()
				.getResponse()
				.getHeader("Location");
			// @formatter:on
		Long id = getIdFromLocation(location);
		assertThat( "entity persisted", repository.exists(id) );
		Image saved = repository.findById(id);
		assertThat(saved.getType(), equalTo("BlueTooth Image"));
	}
	
	@Test
	public void shouldDeleteImage() throws Exception {
		Long id = repository.findFirstByType("small").getId();
		// @formatter:off
		mvc
			.perform( 
					delete("/images/{id}",id)
					)
			.andDo(print())
			.andExpect( status().isNoContent() );
		// @formatter:on
		assertThat("image deleted", ! repository.exists(id));
	}

	@Test
	public void shouldUpdateImage() throws Exception {
		Long id = repository.findFirstByType("small").getId();
		// @formatter:off
		mvc
			.perform( 
					patch("/images/{id}",id)
						.content( mapper.writeValueAsBytes(new Image("extra small")) )
						.contentType(MediaType.APPLICATION_JSON_UTF8)
					)
			.andExpect( status().isOk() );
		// @formatter:on
		Image updated = repository.findById(id);
		assertThat(updated.getType(), equalTo("extra small"));
	}
	
	@After
	public void tearDown() {
		fixture.cleanDatabase();
	}

}
