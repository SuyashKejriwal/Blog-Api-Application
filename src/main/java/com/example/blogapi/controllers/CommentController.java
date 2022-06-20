package com.example.blogapi.controllers;

import com.example.blogapi.payloads.CommentDto;
import com.example.blogapi.service.CommentService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/post/{postId}/user/{userId}")
    public ResponseEntity<CommentDto> createComment(
            @RequestBody CommentDto commentDto,
            @PathVariable("postId") Long postId,
            @PathVariable("userId") Long userId
    ){
        return ResponseEntity.ok(commentService.createComment(commentDto,postId,userId));
    }


}
