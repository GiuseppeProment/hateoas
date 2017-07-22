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

import profile.domain.Product;
import profile.repository.ProductRepository;
import profile.service.FixtureService;

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
	private ProductRepository products;

	@Autowired
	private FixtureService fixture;
	
	private byte[] createProductAsJson(String name, String description) throws Exception {
		return mapper.writeValueAsBytes(new Product(name,description));
	}

	private Long getIdFromProductLocation(String location) {
		return Long.valueOf(location.substring(location.lastIndexOf('/')+1));
	}
	
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
		fixture.initializeDatabase();
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
				.andDo(print())
				.andExpect( status().isCreated() )
				.andExpect( header().string("Location", startsWith("http://localhost/products/")))
				.andReturn()
				.getResponse()
				.getHeader("Location");
			// @formatter:on
		Long id = getIdFromProductLocation(location);
		assertThat( "entity persisted", products.exists(id) );
		Product savedProduct = products.findById(id);
		assertThat(savedProduct.getName(), equalTo("Flip-4"));
		assertThat(savedProduct.getDescription(), equalTo("BlueTooth Speaker"));
	}
	
	@Test
	public void shouldDeleteProduct() throws Exception {
		Long id = products.findFirstByName("wheel").getId();
		// @formatter:off
		mvc
			.perform( 
					delete("/products/{id}",id)
					)
			.andExpect( status().isNoContent() );
		// @formatter:on
		assertThat("product deleted", ! products.exists(id));
	}

	@Test
	public void shouldUpdateProduct() throws Exception {
		Long id = products.findFirstByName("bike").getId();
		// @formatter:off
		mvc
			.perform( 
					patch("/products/{id}",id)
						.content( mapper.writeValueAsBytes(new Product("new description for bike")) )
						.contentType(MediaType.APPLICATION_JSON_UTF8)
					)
			.andExpect( status().isOk() );
		// @formatter:on
		Product product = products.findById(id);
		assertThat(product.getDescription(), equalTo("new description for bike"));
	}
	
	@Test
	public void shouldGetAllProductExcludingRelationships() throws Exception {
		// @formatter:off
		mvc
			.perform( get("/products") )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$._embedded.products.length()", equalTo(3)))
			.andExpect( jsonPath("$._embedded.products[0].name", equalTo("bike")))
			.andExpect( jsonPath("$._embedded.products[0].description", equalTo("basic bike")))
			.andExpect( jsonPath("$._embedded.products[1].name", equalTo("selim")))
			.andExpect( jsonPath("$._embedded.products[1].description", equalTo("basic selim")))
			.andExpect( jsonPath("$._embedded.products[2].name", equalTo("wheel")))
			.andExpect( jsonPath("$._embedded.products[2].description", equalTo("basic wheel")))
			;
		// @formatter:on
	}
	
	@Test
	public void shouldGetProductByIdExcludingRelationships() throws Exception {
		Long id = products.findFirstByName("bike").getId();
		// @formatter:off
		mvc
			.perform( get("/products/{id}",id) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$.name", equalTo("bike")))
			.andExpect( jsonPath("$.description", equalTo("basic bike")))
			;
		// @formatter:on
	}
	
	@Test
	public void shouldGetChildrenForSpecificProduct() throws Exception {
		Long id = products.findFirstByName("bike").getId();
		// @formatter:off
		mvc
			.perform( get("/products/{id}/products",id) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$._embedded.products.length()", equalTo(2)))
			.andExpect( jsonPath("$._embedded.products[0].name", equalTo("selim")))
			.andExpect( jsonPath("$._embedded.products[0].description", equalTo("basic selim")))
			.andExpect( jsonPath("$._embedded.products[1].name", equalTo("wheel")))
			.andExpect( jsonPath("$._embedded.products[1].description", equalTo("basic wheel")))
			;
		// @formatter:on
	}
	
	@Test
	public void shouldGetImagesForSpecificProduct() throws Exception {
		Long id = products.findFirstByName("bike").getId();
		// @formatter:off
		mvc
			.perform( get("/products/{id}/images",id) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$._embedded.images.length()", equalTo(2)))
			.andExpect( jsonPath("$._embedded.images[0].type", equalTo("medium")))
			.andExpect( jsonPath("$._embedded.images[1].type", equalTo("small")))
			;
		// @formatter:on
	}
	
	@Test
	public void shouldGetAllProductsWithChildrenAndImages() throws Exception {
		// @formatter:off
		mvc
			.perform( get("/products?projection=full") )
			
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$._embedded.products.length()", equalTo(3)))
			
			.andExpect( jsonPath("$._embedded.products[0].name", equalTo("bike")))
			.andExpect( jsonPath("$._embedded.products[0].description", equalTo("basic bike")))
			.andExpect( jsonPath("$._embedded.products[0].images.length()", equalTo(2)))
			.andExpect( jsonPath("$._embedded.products[0].images[0].type", equalTo("medium")))
			.andExpect( jsonPath("$._embedded.products[0].images[1].type", equalTo("small")))
			.andExpect( jsonPath("$._embedded.products[0].products.length()", equalTo(2)))
			.andExpect( jsonPath("$._embedded.products[0].products[0].name", equalTo("selim")))
			.andExpect( jsonPath("$._embedded.products[0].products[1].name", equalTo("wheel")))
			
			.andExpect( jsonPath("$._embedded.products[1].name", equalTo("selim")))
			.andExpect( jsonPath("$._embedded.products[1].description", equalTo("basic selim")))
			.andExpect( jsonPath("$._embedded.products[1].images.length()", equalTo(2)))
			.andExpect( jsonPath("$._embedded.products[1].images[0].type", equalTo("medium")))
			.andExpect( jsonPath("$._embedded.products[1].images[1].type", equalTo("small")))
			.andExpect( jsonPath("$._embedded.products[1].products.length()", equalTo(0)))
			
			.andExpect( jsonPath("$._embedded.products[2].name", equalTo("wheel")))
			.andExpect( jsonPath("$._embedded.products[2].description", equalTo("basic wheel")))
			.andExpect( jsonPath("$._embedded.products[2].images.length()", equalTo(2)))
			.andExpect( jsonPath("$._embedded.products[2].images[0].type", equalTo("medium")))
			.andExpect( jsonPath("$._embedded.products[2].images[1].type", equalTo("small")))
			.andExpect( jsonPath("$._embedded.products[2].products.length()", equalTo(0)))
			;
		// @formatter:on
	}
	
	@Test
	public void shouldGetAllProductsWithChildren() throws Exception {
		// @formatter:off
		mvc
			.perform( get("/products?projection=children") )
			
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$._embedded.products.length()", equalTo(3)))
			
			.andExpect( jsonPath("$._embedded.products[0].name", equalTo("bike")))
			.andExpect( jsonPath("$._embedded.products[0].description", equalTo("basic bike")))
			.andExpect( jsonPath("$._embedded.products[0].products.length()", equalTo(2)))
			.andExpect( jsonPath("$._embedded.products[0].products[0].name", equalTo("selim")))
			.andExpect( jsonPath("$._embedded.products[0].products[1].name", equalTo("wheel")))
			
			.andExpect( jsonPath("$._embedded.products[1].name", equalTo("selim")))
			.andExpect( jsonPath("$._embedded.products[1].description", equalTo("basic selim")))
			.andExpect( jsonPath("$._embedded.products[1].products.length()", equalTo(0)))
			
			.andExpect( jsonPath("$._embedded.products[2].name", equalTo("wheel")))
			.andExpect( jsonPath("$._embedded.products[2].description", equalTo("basic wheel")))
			.andExpect( jsonPath("$._embedded.products[2].products.length()", equalTo(0)));
		// @formatter:on
	}
	
	@Test
	public void shouldGetAllProductsWithImages() throws Exception {
		// @formatter:off
		mvc
			.perform( get("/products?projection=images") )
			
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$._embedded.products.length()", equalTo(3)))
			
			.andExpect( jsonPath("$._embedded.products[0].name", equalTo("bike")))
			.andExpect( jsonPath("$._embedded.products[0].description", equalTo("basic bike")))
			.andExpect( jsonPath("$._embedded.products[0].images.length()", equalTo(2)))
			.andExpect( jsonPath("$._embedded.products[0].images[0].type", equalTo("medium")))
			.andExpect( jsonPath("$._embedded.products[0].images[1].type", equalTo("small")))
			
			.andExpect( jsonPath("$._embedded.products[1].name", equalTo("selim")))
			.andExpect( jsonPath("$._embedded.products[1].description", equalTo("basic selim")))
			.andExpect( jsonPath("$._embedded.products[1].images.length()", equalTo(2)))
			.andExpect( jsonPath("$._embedded.products[1].images[0].type", equalTo("medium")))
			.andExpect( jsonPath("$._embedded.products[1].images[1].type", equalTo("small")))
			
			.andExpect( jsonPath("$._embedded.products[2].name", equalTo("wheel")))
			.andExpect( jsonPath("$._embedded.products[2].description", equalTo("basic wheel")))
			.andExpect( jsonPath("$._embedded.products[2].images.length()", equalTo(2)))
			.andExpect( jsonPath("$._embedded.products[2].images[0].type", equalTo("medium")))
			.andExpect( jsonPath("$._embedded.products[2].images[1].type", equalTo("small")))
			;
		// @formatter:on
	}
	
	@Test
	public void shouldGetProductByIdWithImages() throws Exception {
		Long id = products.findFirstByName("bike").getId();
		// @formatter:off
		mvc
			.perform( get("/products/{id}?projection=images",id) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$.name", equalTo("bike")))
			.andExpect( jsonPath("$.description", equalTo("basic bike")))
			.andExpect( jsonPath("$.images.length()", equalTo(2)))
			.andExpect( jsonPath("$.images[0].type", equalTo("medium")))
			.andExpect( jsonPath("$.images[1].type", equalTo("small")))
			;
		// @formatter:on
	}

	@Test
	public void shouldGetProductByIdWithChildren() throws Exception {
		Long id = products.findFirstByName("bike").getId();
		// @formatter:off
		mvc
			.perform( get("/products/{id}?projection=children",id) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$.name", equalTo("bike")))
			.andExpect( jsonPath("$.description", equalTo("basic bike")))
			.andExpect( jsonPath("$.products.length()", equalTo(2)))
			.andExpect( jsonPath("$.products[0].name", equalTo("selim")))
			.andExpect( jsonPath("$.products[1].name", equalTo("wheel")))
			;
		// @formatter:on
	}

	@Test
	public void shouldGetProductByIdWithChildrenAndImages() throws Exception {
		Long id = products.findFirstByName("bike").getId();
		// @formatter:off
		mvc
			.perform( get("/products/{id}?projection=full",id) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("$.name", equalTo("bike")))
			.andExpect( jsonPath("$.description", equalTo("basic bike")))
			.andExpect( jsonPath("$.images.length()", equalTo(2)))
			.andExpect( jsonPath("$.images[0].type", equalTo("medium")))
			.andExpect( jsonPath("$.images[1].type", equalTo("small")))
			.andExpect( jsonPath("$.products.length()", equalTo(2)))
			.andExpect( jsonPath("$.products[0].name", equalTo("selim")))
			.andExpect( jsonPath("$.products[1].name", equalTo("wheel")))
			;
		// @formatter:on
	}

	
	@After
	public void tearDown() {
		fixture.cleanDatabase();
	}

}
