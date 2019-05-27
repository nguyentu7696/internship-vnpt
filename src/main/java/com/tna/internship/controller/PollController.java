package com.tna.internship.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.tna.internship.entity.Poll;
import com.tna.internship.playload.ChoiceRequest;
import com.tna.internship.playload.PollRequest;
import com.tna.internship.playload.PollResponse;
import com.tna.internship.playload.SignUpRequest;
import com.tna.internship.security.UserPrincipal;
import com.tna.internship.service.PollService;

@Controller
public class PollController {
	
	@Autowired
	private PollService pollService;
	
	@GetMapping("/")
	public String home(Model model, UserPrincipal userPrincipal) {
		List<PollResponse> polls = pollService.getAllPolls(userPrincipal);
		model.addAttribute("polls", polls);
		return "index";
	}
	
	@GetMapping("/create-poll")
	public String showCreatePollPage(Model model, PollRequest pollRequest) {
		for (int i = 1; i < 3; i++) {
			pollRequest.addChoice(new ChoiceRequest());
		}
		model.addAttribute("pollresquest", pollRequest);
		return "createPoll";
	}
	
	
	@PostMapping("/create-poll")
	public String createPoll(Model model,
			@Valid @ModelAttribute("pollRequest") PollRequest pollRequest) {
		Poll poll = pollService.createPoll(pollRequest);
		return "redirect:/";
	}
}
