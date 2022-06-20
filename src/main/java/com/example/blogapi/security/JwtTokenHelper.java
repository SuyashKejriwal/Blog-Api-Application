package com.example.blogapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenHelper {

    public static final long JWT_TOKEN_VALIDITY=5*60*60;

    private String secret="jwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKey" +
            "jwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKey"+
            "jwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKey" +
            "jwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKey"+
            "jwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKey" +
            "jwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKeyjwtTokenKey" ;

    //retrieve username from jwt token
//    public String getUsernameFromToken(String token){
//        return getClaimFromToken(token, Claims::getSubject);
//    }
//
//    //retrieve expiration date form token
//    public Date getExpirationDateFromToken(String token){
//        return getClaimFromToken(token, Claims::getExpiration);
//    }
//
//    public <T>  T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
//        final Claims claims=getAllClaimsFromToken(token);
//        return claimsResolver.apply(claims);
//    }
//
//    //for retrieving any info from token we will need the secret key
//    private Claims getAllClaimsFromToken(String token){
//        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//    }
//
//    //check if token has expired
//    private Boolean isTokenExpired(String token){
//        final Date expiration=getExpirationDateFromToken(token);
//        return expiration.before(new Date());
//    }
//
//    //generate token for user
//    public String generateToken(UserDetails userDetails){
//        Map<String,Object> claims=new HashMap<>();
//        return doGenerateToken(claims,userDetails.getUsername());
//    }
//
//    //while creating the token -
//    //1. Define the claims of token, like Issuer,expiration,subject and the Id
//
//
//    private String doGenerateToken(Map<String,Object> claims,String subject){
//        return Jwts.builder().setClaims(claims)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis()+ JWT_TOKEN_VALIDITY*1000))
//                .signWith(SignatureAlgorithm.HS256,secret).compact();
//    }
//
//    //validate token
//    public Boolean validateToken(String token,UserDetails userDetails){
//        final String username=getUsernameFromToken(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//
//    }

    public String generateToken(String email) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withIssuer("YOUR APPLICATION/PROJECT/COMPANY NAME")
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveSubject(String token)throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("YOUR APPLICATION/PROJECT/COMPANY NAME")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }
}
