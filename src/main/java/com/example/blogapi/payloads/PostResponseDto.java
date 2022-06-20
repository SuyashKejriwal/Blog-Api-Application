package com.example.blogapi.payloads;

import com.example.blogapi.entity.Category;
import com.example.blogapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDto {

    private Long postId;

    private String title;

    private String content;

    private String imageName;

    private String createdDate;

    private UserDto user;

    private CategoryDto category;
}
