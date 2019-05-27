package com.tna.internship.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tna.internship.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByEmail(String email);
	
	Optional<User> findByUsernameOrEmail(String userName, String email);
	
	List<User> findByIdIn(List<Long> userIds);
	
	Optional<User> findByUsername(String userName);
	
	Boolean existsByUsername(String username);
	
	Boolean existsByEmail(String email);

}
