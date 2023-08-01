package com.ayush.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ayush.blog.entities.Category;
import com.ayush.blog.entities.Post;
import com.ayush.blog.entities.User;
import com.ayush.blog.exceptions.ResourceNotFoundException;
import com.ayush.blog.payloads.PostDto;
import com.ayush.blog.payloads.PostResponse;
import com.ayush.blog.repositories.CategoryRepo;
import com.ayush.blog.repositories.PostRepo;
import com.ayush.blog.repositories.UserRepo;
import com.ayush.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;

	@Override
	public PostDto createPost(PostDto postDto, int userId, int categoryId) {
		
		User user= this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","user id", userId));
		Category category=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","category id", categoryId));
		
		
		Post post=this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setCategory(category);
		post.setUser(user);
		
		Post createdPost=this.postRepo.save(post);
		return this.modelMapper.map(createdPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, int postId) {
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("post","post id", postId));
		post.setTitle(postDto.getTitle());
		post.setPostContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		Post updatedPost=this.postRepo.save(post);
		return this.modelMapper.map(updatedPost, PostDto.class);
	}

	@Override
	public void deletePost(int postId) {
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("post","post id", postId));
		this.postRepo.delete(post);
	}

	@Override
	public PostResponse getAllPost(int pageNumber, int pageSize, String sortBy) {
		PostResponse postResponse=new PostResponse();
		Pageable pageable=PageRequest.of(pageNumber, pageSize,Sort.by(sortBy));
		Page<Post> page=this.postRepo.findAll(pageable);
		List<Post> allPost=page.getContent();
		List<PostDto> allPostDtos=allPost.stream().map((i)->this.modelMapper.map(i, PostDto.class)).collect(Collectors.toList());
		postResponse.setContent(allPostDtos);
		postResponse.setPageNumber(page.getNumber());
		postResponse.setPageSize(page.getSize());
		postResponse.setTotalElements(page.getTotalElements());
		postResponse.setTotalPages(page.getTotalPages());
		postResponse.setLastPage(page.isLast());
		return postResponse;
	}

	@Override
	public PostDto getPostById(int postId) {
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id", postId));
		return this.modelMapper.map(post,PostDto.class);
	}

	@Override
	public List<PostDto> getPostByCategory(int categoryId) {
		Category category=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","category id", categoryId));
		
		List<Post> posts = this.postRepo.findByCategory(category);
		List<PostDto> postDtos=posts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postDtos;
	}

	@Override
	public List<PostDto> getPostByUser(int userId) {
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","user id",userId));
		List<Post> posts = this.postRepo.findByUser(user);
		List<PostDto> postDtos=posts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postDtos;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		List<Post> posts=this.postRepo.findByTitleContaining(keyword);
		List<PostDto> postDtos= posts.stream().map((i)->this.modelMapper.map(i, PostDto.class)).collect(Collectors.toList());
		return postDtos;
	}
	
	

}
