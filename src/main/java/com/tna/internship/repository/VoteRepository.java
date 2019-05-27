package com.tna.internship.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tna.internship.entity.Vote;
import com.tna.internship.playload.ChoiceVoteCount;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
	
	@Query("SELECT NEW com.tna.internship.playload.ChoiceVoteCount(v.choice.id, count(v.id) ) "
			+ "FROM Vote v "
			+ "WHERE v.poll.id IN :pollIds "
			+ "GROUP BY v.choice.id")
	List<ChoiceVoteCount> countByPollIdInGroupByChoiceId(@Param("pollIds") List<Long> pollIds);
	
	
	
	@Query("SELECT v "
			+ "FROM Vote v "
			+ "WHERE v.user.id = :userId "
			+ "AND v.choice.id "
			+ "IN :pollIds ")
	List<Vote> findByUserIdAndPollIdIn(@Param("userId") Long userId, @Param("pollIds") List<Long> pollIds);
}
