package com.nefertiti.oath;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.nefertiti.domain.AuthenticationProvider;
import com.nefertiti.domain.User;
import com.nefertiti.service.UserServices;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	@Autowired
	private UserServices userService;
	
	
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		CustomOauth2User oAuth2User = (CustomOauth2User) authentication.getPrincipal();
		String email = oAuth2User.getEmail();
		User user = userService.getUserByEmail(email);
	
		
		String fullName = oAuth2User.getFullName();
		
		if(user == null) {
			//register as a new user
			userService.createNewUserAfterOAuthLoginSuccess(
					email, fullName, 
					AuthenticationProvider.GOOGLE);
		} else {
			//update existing user
			userService.opdateUserAfterOAuthLogin(
					user, fullName,
					AuthenticationProvider.GOOGLE);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	

}
