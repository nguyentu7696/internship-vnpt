package com.tna.internship.controller;

import java.util.Collections;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tna.internship.entity.Role;
import com.tna.internship.entity.RoleName;
import com.tna.internship.entity.User;
import com.tna.internship.playload.SignUpRequest;
import com.tna.internship.repository.RoleRepository;
import com.tna.internship.repository.UserRepository;

@Controller
@Transactional
public class AuthController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncode;

	@Autowired
	private RoleRepository roleRepo;

	@GetMapping("/login")
	public String showLoginPage(Model model) {
		return "login";
	}

	@GetMapping("/register-user")
	public String ShowRegisterUser(Model model, SignUpRequest signUpRequest) {
		model.addAttribute("signUpRequest", signUpRequest);
		return "registerUser";
	}

	@PostMapping("/register-user")
	public String registerUser(Model model, @Valid @ModelAttribute("signUpRequest") SignUpRequest signUpRequest) {

		if (userRepo.existsByEmail(signUpRequest.getEmail())) {
			model.addAttribute("emailError", "tồn tại email!");
			return "registerUser";
		}

		if (userRepo.existsByUsername(signUpRequest.getUsername())) {
			model.addAttribute("usernameError", "tồn tại user name!");
			return "registerUser";
		}

		// Tạo tài khoản
		User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
				signUpRequest.getPassword());

		user.setPassword(passwordEncode.encode(signUpRequest.getPassword()));

		Role role = roleRepo.findByName(RoleName.ROLE_USER);

		user.setRoles(Collections.singleton(role));

		userRepo.save(user);

		return "redirect:/login";
	}

	@GetMapping("/user/info")
	public String infoUser(Model model) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("userDetails", userDetails);
		return "infoUser";
	}
	
	@GetMapping("/403")
	public String accessDenied() {
		return "403";
	}

}
