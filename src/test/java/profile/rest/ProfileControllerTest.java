package profile.rest;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import profile.repository.PersonRepository;
import profile.view.PersonDto;
import profile.view.PhoneDto;


/**
 * @author giuseppe Spring tests Looks more like a integration tests...
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProfileControllerTest {

	@Autowired
	private WebApplicationContext ctx;

	@Autowired
	private ObjectMapper mapper;
	
	private MockMvc mvc;

	@Autowired
	private PersonRepository repository;

	@Test
	public void loginSuccessfullyTest() throws Exception {
		recordPerson(buildWellKnonwPerson());
		//@formatter:off
		mvc
			.perform( 
				get("/login/jose@gmail.com/secret")
				)
				.andDo(print())
				.andExpect( status().isOk() )
				.andExpect( jsonPath("$.name",is("Jose")))
				.andExpect( jsonPath("$.email",is("jose@gmail.com")))
				.andExpect( jsonPath("$.password").exists())
				.andExpect( jsonPath("$.password",not(is("secret"))))
				.andExpect( jsonPath("$.id").exists())
				.andExpect( jsonPath("$.created").exists())
				.andExpect( jsonPath("$.modified").exists())
				.andExpect( jsonPath("$.last_login").exists())
				.andExpect( jsonPath("$.phones[0].ddd",is("11")))
				.andExpect( jsonPath("$.phones[0].number",is("55890444")))
		;
		//@formatter:on
	}
	
	@Test
	public void perfilSuccessfullyTest() throws Exception {
		String personAsJson = recordPerson(buildWellKnonwPerson());
		PersonDto person = mapper.readValue(personAsJson, PersonDto.class);
		//@formatter:off
		mvc
			.perform( 
				get("/perfil/"+person.getId())
				.header("token", person.getToken())
				)
				.andDo(print())
				.andExpect( status().isOk() )
				.andExpect( jsonPath("$.name",is("Jose")))
				.andExpect( jsonPath("$.email",is("jose@gmail.com")))
				.andExpect( jsonPath("$.password").exists())
				.andExpect( jsonPath("$.id").exists())
				.andExpect( jsonPath("$.created").exists())
				.andExpect( jsonPath("$.modified").exists())
				.andExpect( jsonPath("$.last_login").exists())
				.andExpect( jsonPath("$.phones[0].ddd",is("11")))
				.andExpect( jsonPath("$.phones[0].number",is("55890444")))
		;
		//@formatter:on
	}

	@Test
	public void recordUserSuccessfullyTest() throws Exception {
		PersonDto person = buildWellKnonwPerson();
		String personAsJson = mapper.writeValueAsString(person);
		//@formatter:off
		mvc
			.perform( 
				post("/cadastro")
				.content(personAsJson)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				)
				.andDo(print())
				.andExpect( status().isCreated() )
				.andExpect( jsonPath("$.name",is("Jose")))
				.andExpect( jsonPath("$.email",is("jose@gmail.com")))
				.andExpect( jsonPath("$.password").exists())
				.andExpect( jsonPath("$.password",not(is("my_secret_password"))))
				.andExpect( jsonPath("$.token").exists())
				.andExpect( jsonPath("$.id").exists())
				.andExpect( jsonPath("$.created").exists())
				.andExpect( jsonPath("$.modified").exists())
				.andExpect( jsonPath("$.last_login").exists())
				.andExpect( jsonPath("$.phones[0].ddd",is("11")))
				.andExpect( jsonPath("$.phones[0].number",is("55890444")))
		;
		//@formatter:on
	}

	@Test
	public void recordWithDuplicateEmailTest() throws Exception {
		recordPerson(buildWellKnonwPerson());
		String personAsJson = mapper.writeValueAsString(buildWellKnonwPerson());
		//@formatter:off
		mvc.perform( 
				post("/cadastro")
				.content(personAsJson)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				)
			.andDo(print())
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.mensagem", is("E-mail já existente")));
		//@formatter:on
	}

	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
		repository.deleteAll();
	}

	private PersonDto buildWellKnonwPerson() {
		ArrayList<PhoneDto> phones = new ArrayList<PhoneDto>();
		phones.add(new PhoneDto("11", "55890444"));
		PersonDto person = new PersonDto();
		person.setName("Jose");
		person.setEmail("jose@gmail.com");
		person.setPassword("secret");
		person.setPhones(phones);
		return person;
	}

	private String recordPerson(PersonDto person) throws JsonProcessingException, Exception {
		String json = mapper.writeValueAsString(person);
		//@formatter:off
		return mvc.perform( 
					post("/cadastro")
					.content(json)
					.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn()
				.getResponse()
				.getContentAsString();
		//@formatter:on
	}

}
