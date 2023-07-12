package com.toyota.auth.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Log4j2
@Component
public class JwtService {

    private final String SECRET_KEY = "secretKEYCVQSAPP123Z232321232sszasecretKEYCVQSAPP123Z232321232sszasecretKEYCVQSAPP123Z232321232ssza";


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



    public String generateToken(UserDetails userDetails) {

        StringBuilder roles=new StringBuilder();
        userDetails.getAuthorities().forEach(role->{
            roles.append(role.getAuthority()+" ");
        });
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuer(roles.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 352143522))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }



    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("JwtUtils | validateJwtToken | Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("JwtUtils | validateJwtToken | Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JwtUtils | validateJwtToken | JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JwtUtils | validateJwtToken | JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JwtUtils | validateJwtToken | JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}

