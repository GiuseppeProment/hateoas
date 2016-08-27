package profile.rest;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import profile.rest.exception.ErrorInfo;

/**
 * @author giuseppe
 * My responsibility is handle generics errors.
 */
@RestController
public class ErrorControllerImpl implements ErrorController {

    private static final String PATH = "/error";

	@RequestMapping(value="/error")
	public ErrorInfo error() {
		return new ErrorInfo("url não existe");
	}
    
	@Override
	public String getErrorPath() {
		return PATH;
	}

}
