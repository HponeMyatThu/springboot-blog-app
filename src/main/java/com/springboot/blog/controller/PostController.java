package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.service.impl.PostServiceImpl;
import com.springboot.blog.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService; //only import interface because loose coupling

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //create blog post rest API
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto)
    {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    //get all posts rest api
    @GetMapping
    public PostResponse getAllPost(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir

    )
    {
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    //get post by id rest api
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = AppConstants.DEFAULT_PATH_VARIABLE) long id)
    {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    //update post by id rest api
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable(name = AppConstants.DEFAULT_PATH_VARIABLE) long id)
    {
        PostDto postResponse = postService.updatePost(postDto,id);
        return ResponseEntity.ok(postResponse);
    }

    //delete post by id rest api
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = AppConstants.DEFAULT_PATH_VARIABLE) long id)
    {
        postService.deletePostById(id);
        return new ResponseEntity<>("Post is successfully deleted.", HttpStatus.OK);
    }

}
