package com.ayush.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayush.blog.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer>{
	 

}
