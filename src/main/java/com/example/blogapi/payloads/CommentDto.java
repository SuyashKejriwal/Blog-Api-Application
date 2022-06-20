package com.example.blogapi.payloads;


import com.example.blogapi.entity.Post;
import com.example.blogapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {

    private Long commentId;

    private String content;

    private String commentDate;

    private UserDto userDto;

    private PostResponseDto postDto;
}
