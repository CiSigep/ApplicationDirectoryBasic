package sample.auth;

import sample.model.UserCredentials;

// Dummy Service for authentication of users. Consider using an LDAP or other service to handle authentication,
// rather than having the database store it.
public class DummyAuthenticatorService implements IAuthenticatorService {

	@Override
	public UserCredentials authenticate(String username, String password) {
		
		UserCredentials user = null;
		
		if(("admin".equals(username) && "admin".equals(password)) || ("cisigep".equals(username) && "cisigep".equals(password)))
		{
			 user = new UserCredentials(username);
		}
		
		return user;
		
	}

}
