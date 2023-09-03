package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {
    private CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //create comment rest api
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable (value = "postId") long postId,
                                                    @Valid @RequestBody CommentDto commentDto)
    {

        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    //create get comments by post id
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable (value = "postId") long postId)
    {
        return commentService.getCommentsByPostId(postId);
    }

    //create get comment by id
    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentById(
            @PathVariable (value = "postId") long postId,
            @PathVariable (value = "id") long commentId)
    {

        CommentDto commentDto = commentService.getCommentById(postId, commentId);

        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable (value = "postId") long postId,
            @PathVariable (value = "id") long commentId,
            @Valid @RequestBody CommentDto commentDto
    )
    {
        CommentDto updatedComment = commentService.updateComment(postId, commentId, commentDto);

        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(
            @PathVariable (value = "postId") long postId,
            @PathVariable (value = "id") long commentId
    )
    {
        commentService.deleteComment(postId, commentId);

        return new ResponseEntity<>("Successfully Delete comment.", HttpStatus.OK);
    }
}
