package profile.service;

public interface SecurityService {

	String generateToken();

	String digest(String data);

}