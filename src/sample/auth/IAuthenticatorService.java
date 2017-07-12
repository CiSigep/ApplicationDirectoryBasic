package sample.auth;

import sample.model.UserCredentials;

public interface IAuthenticatorService {
	
	public UserCredentials authenticate(String username, String password);

}
