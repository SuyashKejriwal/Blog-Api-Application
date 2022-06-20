package com.example.blogapi.mapper;

import com.example.blogapi.entity.Category;
import com.example.blogapi.entity.Post;
import com.example.blogapi.entity.User;
import com.example.blogapi.payloads.CategoryDto;
import com.example.blogapi.payloads.PostRequestDto;
import com.example.blogapi.payloads.PostResponseDto;
import com.example.blogapi.payloads.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper(componentModel="spring")
public abstract class PostMapper {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private UserMapper userMapper;

@Mapping(target = "category",source = "category")
@Mapping(target = "user",source = "user")
@Mapping(target = "createdDate",expression = "java(new java.util.Date())")
public abstract Post DtoToEntity(PostRequestDto postRequestDto, User user, Category category);

@Mapping(target = "createdDate",source = "createdDate",qualifiedByName = "parseDateToString")
@Mapping(target = "category",source = "category",qualifiedByName = "categoryMapper")
@Mapping(target = "user",source = "user",qualifiedByName = "userMapper")
public abstract PostResponseDto EntityToDto(Post post);

    @Named("parseDateToString")
    public String parseDateToString(Date date){
        SimpleDateFormat dateformat= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        String stringDate=dateformat.format(date);

        return stringDate;
    }

    @Named("categoryMapper")
    public CategoryDto categoryMapper(Category category){
        return categoryMapper.EntityToDto(category);
    }

    @Named("userMapper")
    public UserDto userMapper(User user){
        return userMapper.EntityToDto(user);
    }
}
