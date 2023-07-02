package com.toyota.auth.service;

import com.toyota.auth.exception.UserAlreadyExistException;
import com.toyota.auth.dto.UserDto;
import com.toyota.auth.entity.User;
import com.toyota.auth.exception.UserNotFoundException;
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

import java.util.Optional;

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
    public String save(UserDto userDto) {
        Optional<User> user1 = userRepository.findByUsername(userDto.getUsername());
        if (user1.isEmpty()){
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
            User user = userRepository.findByUsernameAndDeletedFalse(userRequest.getUsername());
            if (user == null){
                throw new UserNotFoundException("User not found");
            }
            System.out.println(userRequest.getPassword());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(),userRequest.getPassword()));
            return jwtService.generateToken(user);
        }
        catch (AuthenticationException e){
            throw new UserNotFoundException("User not found");
        }

    }

    /**
     * Get User Information  with JWT
     * @param  token - JWT
     * @return  UserDto - User Information
     */
    @CustomLogDebug
    public UserDto getUserDto(String token){
        User user = userRepository.findByUsernameAndDeletedFalse(jwtService.findUsername(token));
        if (user == null){
            throw new UserNotFoundException("User not found");
        }
        return UserDto.builder().username(user.getUsername()).nameSurname(user.getNameSurname()).roles(user.getRoles()).build();
    }
}
