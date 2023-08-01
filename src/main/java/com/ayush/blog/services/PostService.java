package com.ayush.blog.services;

import java.util.List;
import com.ayush.blog.payloads.PostDto;
import com.ayush.blog.payloads.PostResponse;

public interface PostService {

	//create
	PostDto createPost(PostDto postDto, int userId, int categoryId);
	
	//update
	PostDto updatePost(PostDto postDto, int postId);
	
	//delete
	void deletePost(int postId);
	
	//get all post
	PostResponse getAllPost(int pageNumber, int pageSize, String sortBy);
	
	//get post by id
	PostDto getPostById(int postId);
	
	//get post by category
	List<PostDto> getPostByCategory(int categoryId);
	
	//get post by user
	List<PostDto> getPostByUser(int userId);
	
	//search post
	List<PostDto> searchPosts(String keyword);
}
