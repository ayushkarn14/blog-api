package com.ayush.blog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayush.blog.entities.Category;
import com.ayush.blog.entities.Post;
import com.ayush.blog.entities.User;

public interface PostRepo extends JpaRepository<Post, Integer> {
	
	List<Post> findByUser(User user);
	List<Post> findByCategory(Category category);
	List<Post> findByTitleContaining(String keyword);
}