package com.nefertiti.service;

import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nefertiti.domain.User;
import com.nefertiti.repository.UserRepository;

@Service
@Transactional
public class PasswordResetService {
	
	@Autowired
	private UserRepository userRepo;

	public void updateResetPasswordToken(String token, String email) 
			throws EntityNotFoundException {
		
        User user= userRepo.findByEmail(email);
        
        if (user != null) {
        	user.setResetPasswordToken(token);
        	userRepo.save(user);
        } else {
            throw new EntityNotFoundException("Could not find any customer with the email " + email);
        }
    }
     
    public User getByResetPasswordToken(String token) {
    	
        return userRepo.findByResetPasswordToken(token);
    }
     
    public void updatePassword(User user, String newPassword) {
    	
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
         
        user.setResetPasswordToken(null);
        userRepo.save(user);
    }
}
