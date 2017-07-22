package profile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import profile.domain.Image;
import profile.domain.Product;
import profile.repository.ImageRepository;
import profile.repository.ProductRepository;

@Service
public class FixtureService {
	
	@Autowired
	private ProductRepository products;
	
	@Autowired
	private ImageRepository images;

	public void initializeDatabase() {
		Product bike = new Product("bike", "basic bike");
		products.saveAndFlush(bike);
		
		Product selim = new Product("selim", "basic selim", bike);
		products.saveAndFlush(selim);
		
		Product wheel = new Product("wheel", "basic wheel", bike);
		products.saveAndFlush(wheel);
		
		images.saveAndFlush(new Image("small", bike));
		images.saveAndFlush(new Image("medium", bike));
		
		images.saveAndFlush(new Image("small", selim));
		images.saveAndFlush(new Image("medium", selim));
		
		images.saveAndFlush(new Image("small", wheel));
		images.saveAndFlush(new Image("medium", wheel));
	}
	
	public void cleanDatabase() {
		images.deleteAll();
		products.deleteAll();
	}
	
}
