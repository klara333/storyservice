package com.nefertiti.controller;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.nefertiti.domain.User;
import com.nefertiti.service.UserServices;

@Controller
public class RegistrationController {
	
	@Autowired
	private UserServices userService;

	@GetMapping("/register")
	public String showSignUpForm(Model model) {
		
		model.addAttribute("user", new User());
		
		return "signup_form";
		}
	
//	@PostMapping("/process_register")
//    public String processRegister(User user, HttpServletRequest request)
//            throws UnsupportedEncodingException, MessagingException {
//		
//		String email = user.getEmail();
//		User userFromRepo = userService.getUserByEmail(email);
//		
//		if(userFromRepo == null) {
//        userService.register(user, getSiteURL(request));  
//        
//        return "register_success";
//        
//		} else {
//			
//			return "register_fail";
//		}
//    }
	@PostMapping("/process_register")
    public String processRegister(User user, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {

        userService.register(user, getSiteURL(request));  
        
        return "register_success";
    }
	
	public String getSiteURL(HttpServletRequest request) {
		
        String siteURL = request.getRequestURL().toString();
        
        return siteURL.replace(request.getServletPath(), "");
    } 
	
	@GetMapping("/verify")
	public String verifyUser(@Param("code") String code) {
		
	    if (userService.verify(code)) {
	    	
	        return "verify_success";
	    } else {
	    	
	        return "verify_fail";
	    }
	} 
}
