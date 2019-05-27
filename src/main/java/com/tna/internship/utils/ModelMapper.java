package com.tna.internship.utils;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.tna.internship.entity.Poll;
import com.tna.internship.entity.User;
import com.tna.internship.playload.ChoiceResponse;
import com.tna.internship.playload.ChoiceVoteCount;
import com.tna.internship.playload.PollResponse;
import com.tna.internship.playload.UserSummary;

public class ModelMapper {
	public static PollResponse mapPollToPollResponse(Poll poll, Map<Long, Long> choiceVotesMap, User creator,
			Long userVote) {

		PollResponse pollResponse = new PollResponse();
		pollResponse.setId(poll.getId());
		pollResponse.setQuestion(poll.getQuestion());
		pollResponse.setCreationDateTime(poll.getCreatedAt());
		pollResponse.setExpirationDateTime(poll.getExpirationDateTime());

		Instant now = Instant.now();
	//	pollResponse.setExpired(poll.getExpirationDateTime().isBefore(now));

		List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(choice -> {
			ChoiceResponse choiceResponse = new ChoiceResponse();
			choiceResponse.setId(choice.getId());
			choiceResponse.setText(choice.getText());

			if (choiceVotesMap.containsKey(choice.getId())) {
				choiceResponse.setVoteCount(choiceVotesMap.get(choice.getId()));
			} else {
				choiceResponse.setVoteCount(0);
			}

			return choiceResponse;
		}).collect(Collectors.toList());
		
		pollResponse.setChoices(choiceResponses);
		
		UserSummary userSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
		pollResponse.setCreatedBy(userSummary);

		if (userVote != null) {
			pollResponse.setSelectedChoice(userVote);
		}

		long totalVote = pollResponse.getChoices().stream()
				.mapToLong(ChoiceResponse::getVoteCount)
				.sum();
		pollResponse.setTotalVotes(userVote);

		return pollResponse;
	}
}
