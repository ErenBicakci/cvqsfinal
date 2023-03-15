package com.toyota.auth.service;

import com.toyota.auth.dto.UserDto;
import com.toyota.auth.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Log4j2
@Component
public class JwtService {


    private String SECRET_KEY = "secretKEYCVQSAPP123Z232321232sszasecretKEYCVQSAPP123Z232321232sszasecretKEYCVQSAPP123Z232321232ssza";



    public String findUsername(String token) {
        return exportToken(token, Claims::getSubject);
    }





    private <T> T exportToken(String token, Function<Claims,T> claimsTFunction) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();

        return claimsTFunction.apply(claims);
    }


    private Key getKey() {
        byte[] key= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }

    public boolean tokenControl(String jwt, UserDetails userDetails) {
        final String username = findUsername(jwt);
        return (username.equals(userDetails.getUsername()) && !exportToken(jwt,Claims::getExpiration).before(new Date()));
    }


    public String generateToken(UserDetails user) {

        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+100000*60*24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}

