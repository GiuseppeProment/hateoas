package profile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import profile.domain.Product;
import profile.repository.ImageRepository;
import profile.repository.ProductRepository;

/**
 * @author giuseppe 
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductTest {

	@Autowired
	private WebApplicationContext ctx;

	@Autowired
	private ObjectMapper mapper;

	private MockMvc mvc;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ImageRepository imageRepository;

	private byte[] createProductAsJson(String name, String description) throws Exception {
		return mapper.writeValueAsBytes(new Product(name,description));
	}

	private Long getIdFromProductLocation(String location) {
		return Long.valueOf(location.substring(location.lastIndexOf('/')+1));
	}
	
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	@Test
	public void shouldCreateProduct() throws Exception {
		String location = 
			// @formatter:off
			mvc
				.perform( 
						post("/products")
							.content( createProductAsJson("Flip-4","BlueTooth Speaker") )
							.contentType(MediaType.APPLICATION_JSON_UTF8)
						)
				.andExpect( status().isCreated() )
				.andExpect( header().string("Location", startsWith("http://localhost/products/")))
				.andReturn()
				.getResponse()
				.getHeader("Location");
			// @formatter:on
		Long id = getIdFromProductLocation(location);
		assertThat( "entity persisted", productRepository.exists(id) );
		Product savedProduct = productRepository.findById(id);
		assertThat(savedProduct.getName(), equalTo("Flip-4"));
		assertThat(savedProduct.getDescription(), equalTo("BlueTooth Speaker"));
	}
	
	@Test
	public void shouldDeleteProduct() throws Exception {
		productRepository.saveAndFlush( new Product("Flip-3","Old BlueTooth Speaker") );
		Long id = productRepository.findFirstByName("Flip-3").getId();
		// @formatter:off
		mvc
			.perform( 
					delete("/products/"+id)
					)
			.andExpect( status().isNoContent() );
		// @formatter:on
		assertThat("product deleted", ! productRepository.exists(id));
	}

	@Test
	public void shouldUpdateProduct() throws Exception {
		productRepository.saveAndFlush( new Product("Flip-3","Old BlueTooth Speaker") );
		Long id = productRepository.findFirstByName("Flip-3").getId();
		// @formatter:off
		mvc
			.perform( 
					patch("/products/"+id)
						.content( mapper.writeValueAsBytes(new Product("new description for Old BlueTooth Speaker")) )
						.contentType(MediaType.APPLICATION_JSON_UTF8)
					)
			.andExpect( status().isOk() );
		// @formatter:on
		Product product = productRepository.findById(id);
		assertThat(product.getDescription(), equalTo("new description for Old BlueTooth Speaker"));
	}
	
	@Test
	public void shouldGetAllProductExcludingRelationships() throws Exception {
		productRepository.saveAndFlush( new Product("Flip-1","BlueTooth Speaker 1") );
		productRepository.saveAndFlush( new Product("Flip-2","BlueTooth Speaker 2") );
		productRepository.saveAndFlush( new Product("Flip-3","BlueTooth Speaker 3") );
		// @formatter:off
		mvc
			.perform( get("/products") )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$._embedded.products.length()", equalTo(3)))
			.andExpect( jsonPath("$._embedded.products[0].name", equalTo("Flip-1")))
			.andExpect( jsonPath("$._embedded.products[0].description", equalTo("BlueTooth Speaker 1")))
			.andExpect( jsonPath("$._embedded.products[1].name", equalTo("Flip-2")))
			.andExpect( jsonPath("$._embedded.products[1].description", equalTo("BlueTooth Speaker 2")))
			.andExpect( jsonPath("$._embedded.products[2].name", equalTo("Flip-3")))
			.andExpect( jsonPath("$._embedded.products[2].description", equalTo("BlueTooth Speaker 3")))
			;
		// @formatter:on
	}
	
	@Test
	public void shouldGetProductByIdExcludingRelationships() throws Exception {
		productRepository.saveAndFlush( new Product("Flip-3","Old BlueTooth Speaker") );
		Long id = productRepository.findFirstByName("Flip-3").getId();
		// @formatter:off
		mvc
			.perform( get("/products/"+id) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$.name", equalTo("Flip-3")))
			.andExpect( jsonPath("$.description", equalTo("Old BlueTooth Speaker")))
			;
		// @formatter:on
	}
	
	@Test
	public void shouldGetChildProductsForSpecificProduct() throws Exception {
		Product parent = productRepository.saveAndFlush( new Product("ParentOfAll","grand parent") );
		productRepository.saveAndFlush( new Product("son-1","first son",parent) );
		productRepository.saveAndFlush( new Product("son-2","second son",parent) );
		// @formatter:off
		mvc
			.perform( get(String.format("/products/%s/products",parent.getId())) )
			.andDo(print())
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$._embedded.products.length()", equalTo(2)))
			.andExpect( jsonPath("$._embedded.products[0].name", equalTo("son-1")))
			.andExpect( jsonPath("$._embedded.products[0].description", equalTo("first son")))
			.andExpect( jsonPath("$._embedded.products[1].name", equalTo("son-2")))
			.andExpect( jsonPath("$._embedded.products[1].description", equalTo("second son")))
			;
		// @formatter:on
	}
	
	@Test
	public void shouldGetImagesForSpecificProduct() throws Exception {
		Product parent = productRepository.saveAndFlush( new Product("Flip 4","BlueTooth Speaker") );
		imageRepository.saveAndFlush(new Image("small figure",parent));
		imageRepository.saveAndFlush(new Image("medium figure",parent));
		// @formatter:off
		mvc
			.perform( get(String.format("/products/%s/images",parent.getId())) )
			.andDo(print())
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$._embedded.images.length()", equalTo(2)))
			.andExpect( jsonPath("$._embedded.images[0].type", equalTo("small figure")))
			.andExpect( jsonPath("$._embedded.images[1].type", equalTo("medium figure")))
			;
		// @formatter:on
	}
	
	@After
	public void tearDown() {
		productRepository.deleteAll();
	}

}
