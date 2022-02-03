package com.example.demo.mapper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.UserDto;

@Repository
public interface UserRepository extends JpaRepository<UserDto, String>{

	// select * from userinfo where id like '%keyword%';
	public List<UserDto> findByIdContaining(String keyword);
	
	// select * from userinfo where name like '%keyword%';
	public List<UserDto> findByNameContaining(String keyword);
	
	// select * from userinfo where level='keyword';
	public List<UserDto> findByLevel(String keyword);

	// select * from userinfo where desc like '%keyword%';
	public List<UserDto> findByDescContaining(String keyword);
	
}
