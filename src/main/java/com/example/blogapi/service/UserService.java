package com.example.blogapi.service;

import com.example.blogapi.entity.User;
import com.example.blogapi.exceptions.ResourceNotFoundException;
import com.example.blogapi.mapper.UserMapper;
import com.example.blogapi.payloads.UserDto;
import com.example.blogapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserDto createUser(UserDto userDto){
        User user=userMapper.DtoToEntity(userDto);
        User savedUser=userRepository.save(user);
        return userMapper.EntityToDto(savedUser);
    }

    public UserDto updateUser(UserDto userDto, Long userId){
        User user=userRepository.findById(userId).
                orElseThrow(()->new ResourceNotFoundException("User","id",userId));

        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        User updatedUser=userRepository.save(user);
        return userMapper.EntityToDto(updatedUser);
    }

    public UserDto getUserById(Long userId){
        User user=this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","Id",userId));

        return userMapper.EntityToDto(user);
    }

    public List<UserDto> getAllUsers(){
        List<User> users=userRepository.findAll();

        return users.stream().map(userMapper::EntityToDto).collect(Collectors.toList());
    }

    public void deleteUser(Long userId){
       User user= userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","Id",userId));
       userRepository.delete(user);
    }
}
