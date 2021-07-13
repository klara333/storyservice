package com.nefertiti.service;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nefertiti.domain.AuthenticationProvider;
import com.nefertiti.domain.User;
import com.nefertiti.repository.UserRepository;
import net.bytebuddy.utility.RandomString;

@Service
public class UserServices {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String MESSAGE_FROM;

	public void register(User user, String siteUrl)
			throws UnsupportedEncodingException, MessagingException{
			
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);
		user.setEnabled(false);
		
		userRepo.save(user);
		sendVerificationEmail(user, siteUrl);
	
	}

	private void sendVerificationEmail(User user, String siteURL)
	        throws MessagingException, UnsupportedEncodingException {
		
	    String toAddress = user.getEmail();
	    String fromAddress = MESSAGE_FROM;
	    String senderName = "My App";
	    String subject = "Please verify your registration";
	    String content = "Dear [[name]],<br>"
	            + "Please click the link below to verify your registration:<br>"
	            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
	            + "Thank you,<br>"
	            + "Your company name.";
	     
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom(fromAddress, senderName);
	    helper.setTo(toAddress);
	    helper.setSubject(subject);
	     
	    content = content.replace("[[name]]", user.getFullName());
	    String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
	     
	    content = content.replace("[[URL]]", verifyURL);
	    helper.setText(content, true);
	    mailSender.send(message);
	     
	}
	
	public boolean verify(String verificationCode) {
		
	    User user = userRepo.findByVerificationCode(verificationCode);
	     
	    if (user == null || user.isEnabled()) {
	        return false;
	        
	    } else {
	        user.setVerificationCode(null);
	        user.setEnabled(true);
	        userRepo.save(user);
	        
	        return true;
	    }  
	}

	public User getUserByEmail(String email) {
		User user = userRepo.findByEmail(email);
		return user;
	}

	public void createNewUserAfterOAuthLoginSuccess(
			String email,
			String name,
			AuthenticationProvider provider) {
		User user = new User();
		user.setEmail(email);
		user.setEnabled(true);
		user.setFullName(name);
		user.setFirstName("");
		user.setLastName(name);
		user.setPassword("0");
		user.setAuthProvider(provider);
		
		userRepo.save(user);
	}

	public void opdateUserAfterOAuthLogin(User user,
			String fullName, AuthenticationProvider provider) {
		
	user.setFirstName(fullName);
	user.setAuthProvider(provider);
	
	userRepo.save(user);
	}
}
