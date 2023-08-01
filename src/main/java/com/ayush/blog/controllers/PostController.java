package com.ayush.blog.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ayush.blog.payloads.ApiResponse;
import com.ayush.blog.payloads.PostDto;
import com.ayush.blog.payloads.PostResponse;
import com.ayush.blog.services.FileService;
import com.ayush.blog.services.PostService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/")
public class PostController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private FileService fileService;
	
	private String path="images/";
	
	//create
	@PostMapping("user/{userId}/category/{categoryId}/post")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable int userId, @PathVariable int categoryId){
		
		PostDto createdPost=this.postService.createPost(postDto, userId, categoryId);
		return new ResponseEntity<PostDto>(createdPost,HttpStatus.CREATED);
	}
	
	//get by user
	@GetMapping("user/{userId}/post")
	public ResponseEntity<List<PostDto>> getPostByUser(@PathVariable int userId){
		List<PostDto> postDtos=this.postService.getPostByUser(userId);
		return new ResponseEntity<List<PostDto>>(postDtos, HttpStatus.OK);
	}
	
	@GetMapping("category/{categoryId}/post")
	public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable int categoryId){
		List<PostDto> postDtos=this.postService.getPostByCategory(categoryId);
		return new ResponseEntity<List<PostDto>>(postDtos, HttpStatus.OK);
	}
	
	@GetMapping("post")
	public ResponseEntity<PostResponse> getAllPost(@RequestParam(value="pageNumber",defaultValue="0",required=false) int pageNumber, @RequestParam(value="pageSize",defaultValue="5",required=false) int pageSize, @RequestParam(value="sortBy",defaultValue="addedDate",required=false)String sortBy){
		PostResponse response=this.postService.getAllPost(pageNumber, pageSize,sortBy);
		return new ResponseEntity<PostResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping("post/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable int postId){
		PostDto postDto= this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(postDto, HttpStatus.OK);
	}
	
	@DeleteMapping("post/{postId}")
	public ResponseEntity<ApiResponse> deletePostById(@PathVariable int postId) {
		this.postService.deletePost(postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post deleted successfully", true), HttpStatus.OK);
	}
	
	@PutMapping("post/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto,@PathVariable int postId){
		PostDto updatedPost= this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatedPost,HttpStatus.OK);
	}
	
	//search
	@GetMapping("post/search/{keyword}")
	public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keyword")String keyword){
		List<PostDto> res=this.postService.searchPosts(keyword);
		return new ResponseEntity<List<PostDto>>(res,HttpStatus.OK);
	}
	
	@PostMapping("post/image/upload{postId}")
	public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image, @PathVariable int postId ) throws IOException{
		PostDto postDto = this.postService.getPostById(postId);
		String name = this.fileService.uploadImage(path, image);
		postDto.setImageName(name);
		PostDto updatedPost =this.postService.updatePost(postDto, postId);
	
		return new ResponseEntity<PostDto>(updatedPost,HttpStatus.OK);
	}
	
	@GetMapping(value="post/image/{imageName}" , produces=MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response)throws IOException {
		InputStream resource=this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
}
