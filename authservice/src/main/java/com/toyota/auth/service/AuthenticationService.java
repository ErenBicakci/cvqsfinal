package com.toyota.auth.service;

import com.toyota.auth.exception.UserAlreadyExistException;
import com.toyota.auth.dto.UserDto;
import com.toyota.auth.entity.User;
import com.toyota.auth.exception.UserNotFoundException;
import com.toyota.auth.jwt.JwtService;
import com.toyota.auth.log.CustomLogDebug;
import com.toyota.auth.repository.RoleRepository;
import com.toyota.auth.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    public AuthenticationService(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
    }



    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Create User Function
     * @param  userDto - User Information
     * @return  String - JWT
     */

    @CustomLogDebug
    public String registerUser(UserDto userDto) {
        User user1 = userRepository.findByUsername(userDto.getUsername()).orElse(null);
        if (user1 == null){
            User user = User.builder().username(userDto.getUsername()).password(encoder().encode(userDto.getPassword())).nameSurname(userDto.getNameSurname())
                    .roles(roleRepository.findAllByName("ROLE_ADMIN")).deleted(false).build();

            userRepository.save(user);
            return jwtService.generateToken(user);
        }
        throw new UserAlreadyExistException("User already exists");
    }


    /**
     * User Authentication Function
     * @param  userRequest - User Information
     * @return  String - JWT
     */
    @CustomLogDebug
    public String auth(UserDto userRequest) {
        try {
            User user = userRepository.findByUsernameAndDeletedFalse(userRequest.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(),userRequest.getPassword()));
            return jwtService.generateToken(user);
        }
        catch (AuthenticationException e){
            throw new UserNotFoundException("User not found");
        }

    }




    /**
     * Get User Roles with username
     * @param username
     * @return List<String> - User Roles
     */

    @CustomLogDebug
    public List<String> getUserRoles(String username) {

        User user =userRepository.findByUsernameAndDeletedFalse(username).orElse(null);

        if (user != null){
            List<String> roles = user.getRoles().stream().map(x -> x.getName()).toList();
            return roles;
        }

        return new ArrayList<>();
    }

}
