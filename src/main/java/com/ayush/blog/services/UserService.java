package com.ayush.blog.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ayush.blog.entities.User;
import com.ayush.blog.payloads.UserDto;

@Service
public interface UserService {
	
	UserDto createUser(UserDto user);
	UserDto updateUser(UserDto user, Integer userId);
	UserDto getUserById(Integer userId);
	List<UserDto> getAllUsers();
	void deleteUser(Integer userId);
}
