package com.example.blogapi.security;

import com.example.blogapi.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //get token

        String requestToken=request.getHeader("Authorization");

        //Bearer "token"
        System.out.println(requestToken);
        String userName=null;
        String token=null;

        if(requestToken!=null&&requestToken.startsWith("Bearer")){
            token=requestToken.substring(7);
            System.out.println("Token is "+ token);
            userName=jwtTokenHelper.getUsernameFromToken(token);
            System.out.println("username is "+ userName);
            try{
                userName=jwtTokenHelper.getUsernameFromToken(token);
                System.out.println("username is "+ userName);
            }catch (IllegalArgumentException e){
                System.out.println(
                        "Unable to get jwt token"
                );
            }
            catch(ExpiredJwtException e){
                System.out.println("Jwt token has expired");
            }
            catch(MalformedJwtException e){
                System.out.println("invalid jwt");
            }
        }else{
            System.out.println("Jwt token does not begin with bearer");
        }

        if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails= userDetailsService.loadUserByUsername(userName);
            if(jwtTokenHelper.validateToken(token,userDetails)){
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails,
                        null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }else{
                System.out.println("Invalid jwt token");
            }
        }else{
            System.out.println("username is null or context is not null");
        }

        filterChain.doFilter(request,response);

    }
}
