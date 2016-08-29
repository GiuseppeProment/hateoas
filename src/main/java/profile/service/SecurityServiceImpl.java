package profile.service;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {
	@Override
	public String generateToken() {
		return UUID.randomUUID().toString();
	}

	@Override
	public String digest(String data) {
		return DigestUtils.md5Hex(data);
	}
	
}
