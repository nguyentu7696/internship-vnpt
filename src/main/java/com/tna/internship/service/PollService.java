package com.tna.internship.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tna.internship.entity.Choice;
import com.tna.internship.entity.Poll;
import com.tna.internship.entity.User;
import com.tna.internship.entity.Vote;
import com.tna.internship.playload.ChoiceRequest;
import com.tna.internship.playload.ChoiceVoteCount;
import com.tna.internship.playload.PollRequest;
import com.tna.internship.playload.PollResponse;
import com.tna.internship.repository.PollRepository;
import com.tna.internship.repository.UserRepository;
import com.tna.internship.repository.VoteRepository;
import com.tna.internship.security.UserPrincipal;
import com.tna.internship.utils.ModelMapper;

@Service
public class PollService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PollRepository pollRepository;

	@Autowired
	private VoteRepository voteRepository;

	public List<PollResponse> getAllPolls(UserPrincipal userPrincipal) {

		List<Poll> polls = pollRepository.findAll();

		List<Long> pollIds = polls.stream().map(Poll::getId).collect(Collectors.toList());

		Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(pollIds);
		Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(userPrincipal, pollIds);
		Map<Long, User> creatorMap = getPollCreatorMap(polls);

//		List<PollResponse> pollResponses = new ArrayList<>();
//		for (Poll poll : polls) {
//			PollResponse pollResponse = ModelMapper.mapPollToPollResponse(poll, choiceVoteCountMap,
//					creatorMap.get(poll.getCreatedBy()),
//					pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(poll.getId(), null));
//			pollResponses.add(pollResponse);
//		}

		List<PollResponse> pollResponses = polls.stream().map(poll -> {
			PollResponse pollResponse = ModelMapper.mapPollToPollResponse(poll, choiceVoteCountMap,
					creatorMap.get(poll.getCreatedBy()),
					pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(poll.getId(), null));
			return pollResponse;
		}).collect(Collectors.toList());

		return pollResponses;
	}

	private Map<Long, User> getPollCreatorMap(List<Poll> polls) {
		List<Long> creatorIds = polls.stream().map(Poll::getCreatedBy).distinct().collect(Collectors.toList());

		List<User> creators = userRepository.findByIdIn(creatorIds);
		Map<Long, User> creatorMap = creators.stream().collect(Collectors.toMap(User::getId, Function.identity()));
		return creatorMap;
	}

	private Map<Long, Long> getPollUserVoteMap(UserPrincipal userPrincipal, List<Long> pollIds) {
		Map<Long, Long> pollUserVoteMap = null;
		if (userPrincipal != null) {
			List<Vote> userVotes = voteRepository.findByUserIdAndPollIdIn(userPrincipal.getId(), pollIds);
			pollUserVoteMap = userVotes.stream()
					.collect(Collectors.toMap(vote -> vote.getPoll().getId(), vote -> vote.getChoice().getId()));
		}
		return pollUserVoteMap;
	}

	private Map<Long, Long> getChoiceVoteCountMap(List<Long> pollIds) {
		List<ChoiceVoteCount> votes = voteRepository.countByPollIdInGroupByChoiceId(pollIds);
		Map<Long, Long> choiceVotesMap = votes.stream()
				.collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));
		return choiceVotesMap;
	}

	public void savePoll(PollRequest pollRequest) {

		Poll newpoll = new Poll();
		
		newpoll.setId(pollRequest.getId());

		newpoll.setQuestion(pollRequest.getQuestion());
		
		pollRequest.getChoices().forEach(choiceRequest -> newpoll.addChoice(new Choice(choiceRequest.getText())) );
		
		pollRepository.save(newpoll);
	}

	public PollRequest getPollById(Long id) {
		Optional<Poll> poll = pollRepository.findById(id);
		if(poll.isPresent()) {
			PollRequest pollRequest = new PollRequest();
			pollRequest.setId(id);
			pollRequest.setQuestion(poll.get().getQuestion());
			
			List<ChoiceRequest> choiceRequests = new ArrayList<>();
			for (Choice choice : poll.get().getChoices()) {
				ChoiceRequest choiceRequest = new ChoiceRequest();
				choiceRequest.setText(choice.getText());
				choiceRequests.add(choiceRequest);
			}
			pollRequest.setChoices(choiceRequests);
			
			return pollRequest;
		}
		return null;
	}

}
