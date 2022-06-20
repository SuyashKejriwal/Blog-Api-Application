package com.example.blogapi.mapper;

import com.example.blogapi.entity.User;
import com.example.blogapi.payloads.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class UserMapper {

    public abstract User DtoToEntity(UserDto userDto);

    public abstract UserDto EntityToDto(User user);

}
