package com.example.blogapi.service;

import com.example.blogapi.entity.Category;
import com.example.blogapi.entity.Post;
import com.example.blogapi.entity.User;
import com.example.blogapi.exceptions.ResourceNotFoundException;
import com.example.blogapi.mapper.PostMapper;
import com.example.blogapi.payloads.PostPageResponseDto;
import com.example.blogapi.payloads.PostRequestDto;
import com.example.blogapi.payloads.PostResponseDto;
import com.example.blogapi.repository.CategoryRepository;
import com.example.blogapi.repository.PostRepository;
import com.example.blogapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    //create
    public PostResponseDto createPost(PostRequestDto postRequestDto, Long userId, Long categoryId){
        User user=userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User","Id",userId));
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","Id",categoryId));
        Post post=postMapper.DtoToEntity(postRequestDto,user,category);
        post.setImageName("default.png");
        Post savedPost=postRepository.save(post);
        return postMapper.EntityToDto(savedPost);
    }

    //update
    public PostResponseDto updatePost(PostRequestDto postRequestDto, Long postId){
        Post post=postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","Id",postId));
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setImageName(postRequestDto.getImageName());

        Post updatedPost=postRepository.save(post);
        return postMapper.EntityToDto(updatedPost);
    }

    //delete
    public void deletePost(Long postId){
        Post post=postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","Id",postId));
        postRepository.delete(post);
    }

    //get all posts
   public PostPageResponseDto getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir){

       Pageable page=PageRequest.of(pageNumber,pageSize,
               sortDir.equalsIgnoreCase("asc") ?Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

       Page<Post> pagePost=postRepository.findAll(page);


       List<Post> allPosts=pagePost.getContent();

        List<PostResponseDto> content=allPosts.stream()
                .map(postMapper::EntityToDto)
                .collect(Collectors.toList());

        PostPageResponseDto response=PostPageResponseDto.builder()
                .content(content)
                .pageNumber(pagePost.getNumber())
                .pageSize(pagePost.getSize())
                .totalElements(pagePost.getTotalElements())
                .totalPages(pagePost.getTotalPages())
                .lastPage(pagePost.isLast())
                .build();

        return response;
    }

    //get single post
   public PostResponseDto getPostById(Long postId){
        Post post=postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","Id",postId));
        return postMapper.EntityToDto(post);
    }

    //get all posts by category
    public List<PostResponseDto> getPostsByCategory(Long categoryId){

        Category cat=categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category","Id",categoryId));
        List<Post> posts=postRepository.findByCategory(cat);

        return posts.stream()
                .map(postMapper::EntityToDto).collect(Collectors.toList());
    }

    //get all posts by user.
    public List<PostResponseDto> getPostsByUser(Long userId){
        User user=userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User","Id",userId)
        );
        List<Post> posts=postRepository.findByUser(user);

        return posts.stream()
                .map(postMapper::EntityToDto).collect(Collectors.toList());
    }

    //search post by keyword
    public List<PostResponseDto> searchPosts(String keyword){
        List<Post> posts=postRepository.searchByTitle("%"+keyword+"%");
        return posts.stream()
                .map(postMapper::EntityToDto).collect(Collectors.toList());
    }

}
