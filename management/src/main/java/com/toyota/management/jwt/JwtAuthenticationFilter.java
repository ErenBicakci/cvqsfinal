package com.toyota.management.jwt;

import com.toyota.management.client.ManagementClient;
import com.toyota.management.dto.TokenControl;
import com.toyota.management.entity.Role;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {




    private final ManagementClient managementClient;



    public JwtAuthenticationFilter( ManagementClient managementClient) {


        this.managementClient = managementClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        try {

            String jwt = parseJwt(req);
            TokenControl tokenControl = managementClient.getAuthorities(jwt);
            if (jwt != null && tokenControl.isStatus()) {


                List<SimpleGrantedAuthority> authorities=new ArrayList<>();


                for(Role role : tokenControl.getRoles()){
                    authorities.add(new SimpleGrantedAuthority(role.getName()));
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        tokenControl.getUsername(), null, authorities);
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
