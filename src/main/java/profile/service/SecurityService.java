package profile.service;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
	public String generateToken() {
		return UUID.randomUUID().toString();
	}

	public String digest(String data) {
		return DigestUtils.md5Hex(data);
	}
	
}
