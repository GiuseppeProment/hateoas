package profile.service;

import profile.dto.PersonDto;
import profile.exception.DuplicateEmailException;
import profile.exception.InvalidSessionException;
import profile.exception.InvalidTokenException;
import profile.exception.UnauthorizedException;
import profile.exception.UserIdNotFoundException;
import profile.exception.UserNotFoundException;

public interface ProfileService {

	PersonDto cadastro(PersonDto personDto) throws DuplicateEmailException;

	PersonDto login(String email, String password) throws UserNotFoundException, UnauthorizedException;

	PersonDto perfil(String token, String userIdAsString)
			throws InvalidTokenException, InvalidSessionException, UserIdNotFoundException;

}