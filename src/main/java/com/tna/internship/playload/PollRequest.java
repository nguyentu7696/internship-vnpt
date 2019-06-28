package com.tna.internship.playload;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PollRequest {
	
	private Long id;
	
	@NotBlank
	@Size(max = 140)
	private String question;

	
	@Size(min = 2, max = 6)
	@Valid
	private List<ChoiceRequest> choices = new ArrayList<>();

//	@NotNull
	@Valid
	private PollLength pollLength;
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<ChoiceRequest> getChoices() {
		return choices;
	}

	public void setChoices(List<ChoiceRequest> choices) {
		this.choices = choices;
	}

	public PollLength getPollLength() {
		return pollLength;
	}

	public void setPollLength(PollLength pollLength) {
		this.pollLength = pollLength;
	}
	
	public void addChoice(ChoiceRequest choiceRequest) {
		this.choices.add(choiceRequest);
	}
}
