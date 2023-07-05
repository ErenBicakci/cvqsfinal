package com.toyota.management.jwt;

import com.toyota.management.client.ManagementClient;
import com.toyota.management.exception.GenericException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {



    private final JwtService jwtService;
    private final ManagementClient managementClient;



    public JwtAuthenticationFilter(JwtService jwtService, ManagementClient managementClient) {
        this.jwtService = jwtService;

        this.managementClient = managementClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        try {

            String jwt = parseJwt(req);

            if (jwt != null && jwtService.validateJwtToken(jwt)) {

                String username = jwtService.findUsername(jwt);
                List<SimpleGrantedAuthority> authorities=new ArrayList<>();
                for(String rolename : managementClient.getAuthorities(username)){
                    authorities.add(new SimpleGrantedAuthority(rolename));
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
                throw new GenericException("Token is not valid" + e.getMessage());
        }

        chain.doFilter(req, res);
    }

    private String parseJwt(HttpServletRequest request) {

        String headerAuth = request.getHeader("Authorization");

        //   if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
        if (StringUtils.hasText(headerAuth)) {


            return headerAuth.substring(7,
                    headerAuth.length());
        }

        return null;
    }

}
