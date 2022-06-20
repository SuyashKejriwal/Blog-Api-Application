package com.example.blogapi.service;

import com.example.blogapi.entity.Comment;
import com.example.blogapi.entity.Post;
import com.example.blogapi.entity.User;
import com.example.blogapi.exceptions.ResourceNotFoundException;
import com.example.blogapi.mapper.CommentMapper;
import com.example.blogapi.payloads.CommentDto;
import com.example.blogapi.repository.CommentRepository;
import com.example.blogapi.repository.PostRepository;
import com.example.blogapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public CommentDto createComment(CommentDto commentDto,Long postId,Long userId){

        User user=userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User","Id",userId));
        Post post=postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","Id",postId));
        Comment comment=commentMapper.DtoToEntity(commentDto,post,user);
        Comment savedComment=commentRepository.save(comment);
        return commentMapper.EntityToDto(savedComment);
    }

    public void deleteComment(Long commentId){
        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("Comment","Id",commentId));
        commentRepository.delete(comment);
    }

    public CommentDto updateComment(CommentDto commentDto,Long commentId){
        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("Comment","Id",commentId));
        comment.setContent(commentDto.getContent());
        Comment updatedComment=commentRepository.save(comment);
        return commentMapper.EntityToDto(updatedComment);
    }


}
