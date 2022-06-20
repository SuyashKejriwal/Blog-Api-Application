package com.example.blogapi.controllers;

import com.example.blogapi.config.AppConstants;
import com.example.blogapi.payloads.ApiResponse;
import com.example.blogapi.payloads.PostPageResponseDto;
import com.example.blogapi.payloads.PostRequestDto;
import com.example.blogapi.payloads.PostResponseDto;
import com.example.blogapi.service.FileService;
import com.example.blogapi.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @PostMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<PostResponseDto> createPost(
            @RequestBody PostRequestDto postRequestDto,
            @PathVariable("userId") Long userId,
            @PathVariable("categoryId") Long categoryId){
        PostResponseDto postResponseDto=postService.createPost(postRequestDto,userId,categoryId);
       // System.out.println("Post response is "+ postResponseDto);
        return new ResponseEntity<>(postResponseDto,
        HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponseDto>> getPostsByUser(@PathVariable("userId") Long userId){
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PostResponseDto>> getPostsByCategory(@PathVariable("categoryId") Long categoryId){
        return ResponseEntity.ok(postService.getPostsByCategory(categoryId));
    }

    @GetMapping("")
    public ResponseEntity<PostPageResponseDto> getAllPosts(
            @RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(value="sortBy",defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false) String sortDir
    ){
        return ResponseEntity.ok(postService.getAllPost(pageNumber,pageSize,sortBy,sortDir));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPostsById(@PathVariable("postId") Long postId){
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePosts(@RequestBody PostRequestDto postRequestDto,
                                                       @PathVariable("postId") Long postId){
        return ResponseEntity.ok(postService.updatePost(postRequestDto,postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable("postId") Long postId){
        postService.deletePost(postId);
        return  ResponseEntity.ok(new ApiResponse("Post Deleted Successfully",true));
    }

    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<PostResponseDto>> searchPostsByTitle(
            @PathVariable("keywords") String keywords
    ){
        return ResponseEntity.ok(postService.searchPosts(keywords));
    }

    //post image upload
    @PostMapping("/image/upload/{postId}")
    public ResponseEntity<PostResponseDto> uploadPostImage(@RequestParam("image") MultipartFile image,
                                                         @PathVariable("postId") Long postId) throws IOException {
        PostResponseDto postResponseDto=postService.getPostById(postId);

        String fileName=fileService.uploadImage(path,image);


        postResponseDto.setImageName(fileName);

        PostRequestDto postRequestDto=PostRequestDto.builder()
                        .title(postResponseDto.getTitle())
                                .content(postResponseDto.getContent())
                                        .imageName(postResponseDto.getImageName())
                                                .build();
        PostResponseDto updatedPost=postService.updatePost(postRequestDto,postId);

        return ResponseEntity.ok(updatedPost);

    }

    //method to serve file
    @GetMapping(value = "/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response
    ) throws IOException{
        InputStream resource=fileService.getResource(path,imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}
