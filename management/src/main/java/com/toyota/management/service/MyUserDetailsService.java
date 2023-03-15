package com.toyota.management.service;

import com.toyota.management.client.AuthClient;
import com.toyota.management.dto.UserDto;
import com.toyota.management.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final AuthClient authClient;

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {

        UserDto userDto = authClient.getUserByToken(token).getBody();
        UserDetails userDetails = User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .roles(userDto.getRoles())
                .nameSurname(userDto.getNameSurname())
                .build();
        return userDetails;
    }

}
