package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.expection.BlogAPIException;
import com.springboot.blog.expection.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);

        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        //set post to comment entity
        comment.setPost(post);

        //comment entity to DB
        Comment newComment = commentRepository.save(comment);

        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        //retrieve comments by postId
        List<Comment> comments = commentRepository.findByPostId(postId);

        //convert list of comments entities to list of dtos
        return comments.stream()
                .map(comment -> mapToDto(comment))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {

        Comment comment = getCommentForPost(postId, commentId);

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentRequest) {
        Comment comment = getCommentForPost(postId, commentId);

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Comment comment = getCommentForPost(postId, commentId);

        commentRepository.delete(comment);
    }

    private Comment getCommentForPost(long postId, long commentId) {
        // Retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // Retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId));

        //!comment.getPost().getId().equals(post.getId())
        if (comment.getPost().getId() != post.getId()) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        return comment;
    }

    //convert Entity to DTO
    private CommentDto mapToDto(Comment comment)
    {
        CommentDto commentDto = mapper.map(comment, CommentDto.class);

        /*CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setBody(comment.getBody());
        commentDto.setEmail(comment.getEmail());
        commentDto.setName(comment.getName());*/

        return commentDto;
    }

    //convert DTO To Entity
    private Comment mapToEntity(CommentDto commentDto)
    {
        Comment comment = mapper.map(commentDto, Comment.class);

        /*Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());
        comment.setName(commentDto.getName());*/

        return comment;
    }
}
