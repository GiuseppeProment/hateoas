package profile.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import profile.rest.exception.DuplicateEmailException;
import profile.rest.exception.InvalidSessionException;
import profile.rest.exception.InvalidTokenException;
import profile.rest.exception.UnauthorizedException;
import profile.rest.exception.UserNotFoundException;
import profile.service.ProfileService;
import profile.view.PersonDto;

@RestController
public class ProfileController {
    
	@Autowired
	ProfileService service;

	@RequestMapping(value="/",produces=MediaType.TEXT_HTML_VALUE)
	public String hello() {
		return "<!DOCTYPE html>"
				+ "<html><body>"
				+ "<b>Available urls</b>"
				+ "<p>/cadastro (post with json body)</p>"
				+ "<p>/login/email/password</p>"
				+ "<p>/perfil/userId</p>"
				+ "</body></html>";		
	}
	
    @RequestMapping(value="/cadastro", method=RequestMethod.POST )
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDto cadastro(  @RequestBody PersonDto person ) throws DuplicateEmailException {
    	return service.record(person);
    }
    
    @RequestMapping("/login/{email}/{password}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDto login(  
    		@PathVariable String email, @PathVariable String password ) throws UserNotFoundException, UnauthorizedException {
        return service.login(email,password);
    }
    
    @RequestMapping("/perfil/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDto perfil( @RequestHeader("token") String token, @PathVariable String userId ) throws InvalidTokenException, InvalidSessionException {
        return service.perfil(token, userId);
    }
    
}
