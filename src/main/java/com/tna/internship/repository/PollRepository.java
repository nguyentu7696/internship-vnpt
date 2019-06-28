package com.tna.internship.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tna.internship.entity.Poll;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long>{
	boolean existsById(Long id);
}
