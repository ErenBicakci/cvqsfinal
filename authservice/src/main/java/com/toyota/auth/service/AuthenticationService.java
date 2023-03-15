package com.toyota.auth.service;

import com.toyota.auth.exception.UserAlreadyExistException;
import com.toyota.auth.dto.UserDto;
import com.toyota.auth.entity.User;
import com.toyota.auth.exception.UserNotFoundException;
import com.toyota.auth.repository.RoleRepository;
import com.toyota.auth.repository.UserRepository;
import com.toyota.auth.utility.StringManipulation;
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
    private final StringManipulation stringManipulation;

    public AuthenticationService(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager, RoleRepository roleRepository, StringManipulation stringManipulation) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.stringManipulation = stringManipulation;
    }



    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Kullanıcı Oluşturma Fonksiyonu
     * @param  userDto - Kullanıcı Bilgileri
     * @return  String - JWT
     */

    public String save(UserDto userDto) {
        log.info("Account creation request  :  (USERNAME) " + userDto.getUsername() );
        Optional<User> user1 = userRepository.findByUsername(userDto.getUsername());
        if (user1.isEmpty()){
            User user = User.builder().username(userDto.getUsername()).password(encoder().encode(userDto.getPassword())).nameSurname(userDto.getNameSurname())
                    .roles(roleRepository.findAllByName("ROLE_ADMIN")).deleted(false).build();

            userRepository.save(user);
            var token = jwtService.generateToken(user);
            log.info("User successfully created : (USERNAME) "+ userDto.getUsername());
            return token;
        }
        log.warn("Received a request to create an user with an existing username : (USERNAME) " +userDto.getUsername());
        throw new UserAlreadyExistException("User already exists");
    }


    /**
     * Kullanıcı Giriş Fonksiyonu
     * @param  userRequest - Kullanıcı Bilgileri
     * @return  String - JWT
     */

    public String auth(UserDto userRequest) {
        try {
            User user = userRepository.findByUsernameAndDeletedFalse(userRequest.getUsername());
            if (user == null){
                log.info("Deleted user login request");
                throw new UserNotFoundException("User not found");
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(),userRequest.getPassword()));

            String token = jwtService.generateToken(user);
            log.info("User Signed In : (USERNAME) " + userRequest.getUsername());
            return token;
        }
        catch (AuthenticationException e){
            log.info("Attempted to login with wrong information : (USERNAME) " + userRequest.getUsername() + " (PASSWORD) " + stringManipulation.passwordHide(userRequest.getPassword()) );
            throw new UserNotFoundException("User not found");
        }
        catch (Exception e){
            log.warn("Error : " + e);
        }
        throw new UserNotFoundException("User not found");

    }

    /**
     * JWT den UserDto çekme fonksiyonu
     * @param  token - JWT
     * @return  UserDto - Kullanıcı Bilgileri
     */
    public UserDto getUserDto(String token){
        User user = userRepository.findByUsernameAndDeletedFalse(jwtService.findUsername(token));
        if (user == null){
            log.warn("Deleted user login request");
            throw new UserNotFoundException("User not found");
        }
        return UserDto.builder().username(user.getUsername()).nameSurname(user.getNameSurname()).roles(user.getRoles()).build();
    }
}
