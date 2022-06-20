package com.example.blogapi.controllers;

import com.example.blogapi.payloads.JwtAuthRequest;
import com.example.blogapi.payloads.JwtAuthResponse;
import com.example.blogapi.security.JwtTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(
            @RequestBody JwtAuthRequest request
            ) throws NoSuchAlgorithmException {
        authenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails=userDetailsService.loadUserByUsername(request.getUsername());
        String token=jwtTokenHelper.generateToken("hello");

        JwtAuthResponse response=new JwtAuthResponse();
        response.setToken(token);
        return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);

    }

    private void authenticate(String userName,String password){
        UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userName,password);
            authenticationManager.authenticate(authToken);
    }

}
