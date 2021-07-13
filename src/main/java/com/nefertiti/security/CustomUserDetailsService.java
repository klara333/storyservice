package com.nefertiti.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.nefertiti.domain.User;
import com.nefertiti.repository.UserRepository;

public class CustomUserDetailsService 
			implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException {
		
		User existsUser = userRepo.findByEmail(username);
		
		if(existsUser == null) {
			throw new UsernameNotFoundException
				("User not foud");
		}
		return new CustomUserDetails(existsUser);
	}

}
