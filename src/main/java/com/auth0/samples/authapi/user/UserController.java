package com.auth0.samples.authapi.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

	private ApplicationUserRepository applicationUserRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserController(ApplicationUserRepository applicationUserRepository,
						  BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.applicationUserRepository = applicationUserRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

//	@PostMapping("/sign-up")
//	public void signUp(@RequestBody ApplicationUser user) {
//		user.setPassword((user.getPassword()));
//		applicationUserRepository.save(user);
//	}


	@GetMapping("/all")
	public List<ApplicationUser> getAllNotes() {
		return applicationUserRepository.findAll();
	}


}
