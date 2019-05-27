package com.tna.internship.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tna.internship.entity.Role;
import com.tna.internship.entity.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	Role findByName(RoleName roleName);
	
	

}
