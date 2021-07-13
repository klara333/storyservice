package com.nefertiti.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.nefertiti.domain.Story;
import com.nefertiti.domain.User;
import com.nefertiti.service.ShowUserService;
import com.nefertiti.service.StoryService;
import com.nefertiti.service.UserServices;

@Controller
public class HomeController {

	@Autowired
	private StoryService storyService;

	@Autowired
	private ShowUserService userService;

	@Autowired
	private UserServices service;

	@RequestMapping("/")
	public String stories(Model model) {

		model.addAttribute("stories", storyService.getStories());

		return "stories";
	}

	@RequestMapping("/login")
	public String login() {

		return "login";
	}


	@RequestMapping("/title/{title}")
	public String searchForUser(@PathVariable(value = "title") String title,
			Model model) throws Exception {

		if (title == null)
			throw new Exception("Nincs ilyen címmel sztorink!");
		model.addAttribute("story", storyService.getSpecificStory(title));

		return "story";
	}

	@RequestMapping("/bloggers")
	public String getAllUsers(Model model) {

		List<User> list = userService.getAllUsers();
		model.addAttribute("users", list);

		return "list_bloggers";
	}

	@RequestMapping(path = { "/edit", "/edit/{id}" })
	public String editEmployeeById(Model model, @PathVariable("id")
	Optional<Long> id) throws Exception {

		User entity = userService.getUserById(id.get());
		model.addAttribute("user", entity);

		return "edit_blogger";
	}

	@PostMapping("/process_update")
	public String processUpdate(User user, Model model) {

		userService.updateUser(user);
		List<User> list = userService.getAllUsers();
		model.addAttribute("users", list);

		return "list_bloggers";
	}

	@GetMapping("/add_story")
	public String storyForm(Model model) {

		Story story = new Story();

		model.addAttribute("story", story);
		return "add_stories";
	}

	@PostMapping("/add_story")
	public String storySubmit(@ModelAttribute Story story,
			HttpServletRequest request,
			Model model) {

		Principal principal = request.getUserPrincipal();
	
		
		String email = principal.getName();
		
		
		User existsUser = service.getUserByEmail(email);
		System.err.println(existsUser);
		story.setUser(existsUser);
		story.setPosted(new Date());

		model.addAttribute("story", story);
		storyService.save(story);

		return "story";
	}
	
//	@GetMapping("/user/me")
//	public Map<String, Object> userDetails(@AuthenticationPrincipal OAuth2User user) {
//	    return user.getAttributes();
//	}
}
