package com.example.blogapi.mapper;

import com.example.blogapi.entity.Comment;
import com.example.blogapi.entity.Post;
import com.example.blogapi.entity.User;
import com.example.blogapi.payloads.CommentDto;
import com.example.blogapi.payloads.PostResponseDto;
import com.example.blogapi.payloads.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper(componentModel="spring")
public abstract class CommentMapper {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Mapping(target="post",source = "post")
    @Mapping(target = "user",source = "user")
    @Mapping(target = "content",source = "commentDto.content")
    @Mapping(target = "createdDate",expression = "java(new java.util.Date())")
    public abstract Comment DtoToEntity(CommentDto commentDto, Post post, User user);

    @Mapping(target = "commentDate",source = "createdDate",qualifiedByName = "parseDateToString")
    @Mapping(target = "userDto",source = "user",qualifiedByName = "userMapper")
    @Mapping(target= "postDto",source="post",qualifiedByName = "postMapper")
    public abstract CommentDto EntityToDto(Comment comment);

    @Named("parseDateToString")
    public String parseDateToString(Date date){
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        String stringDate=dateFormat.format(date);

        return stringDate;
    }

    @Named("userMapper")
    public UserDto userMapper(User user){
        return userMapper.EntityToDto(user);
    }

    @Named("postMapper")
    public PostResponseDto postMapper(Post post){
        return postMapper.EntityToDto(post);
    }
}
