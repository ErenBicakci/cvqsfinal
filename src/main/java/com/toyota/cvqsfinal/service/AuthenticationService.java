package com.toyota.cvqsfinal.service;

import com.toyota.cvqsfinal.dto.UserDto;
import com.toyota.cvqsfinal.dto.UserRequest;
import com.toyota.cvqsfinal.entity.User;
import com.toyota.cvqsfinal.repository.RoleRepository;
import com.toyota.cvqsfinal.repository.UserRepository;
import com.toyota.cvqsfinal.utility.StringManipulation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final StringManipulation stringManipulation;


    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public ResponseEntity<String> save(UserDto userDto) {
        log.info("Account creation request  :  (USERNAME) " + userDto.getUsername() );
        if (userRepository.findByUsername(userDto.getUsername()).isEmpty()){
            User user = User.builder().username(userDto.getUsername()).password(encoder().encode(userDto.getPassword())).nameSurname(userDto.getNameSurname())
                    .roles(roleRepository.findAllByName("ROLE_ADMIN")).deleted(false).build();

            userRepository.save(user);
            var token = jwtService.generateToken(user);
            log.info("User successfully created : (USERNAME) "+ userDto.getUsername());
            return ResponseEntity.ok(token);
        }
        log.warn("Received a request to create an user with an existing username : (USERNAME) " +userDto.getUsername());
        return ResponseEntity.status(400).body("Username already exists : (USERNAME) " +userDto.getUsername());
    }

    public ResponseEntity<String> auth(UserRequest userRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(),userRequest.getPassword()));
            User user = userRepository.findByUsername(userRequest.getUsername()).orElseThrow();
            String token = jwtService.generateToken(user);
            log.info("User Signed In : (USERNAME) " + userRequest.getUsername());
            return ResponseEntity.ok(token);
        }
        catch (AuthenticationException e){
            log.warn("Attempted to login with wrong information : (USERNAME) " + userRequest.getUsername() + " (PASSWORD) " + stringManipulation.passwordHide(userRequest.getPassword()) );
            return ResponseEntity.status(400).body("User not found");
        }
        catch (Exception e){
            log.warn("Error : " + e);
        }
        return ResponseEntity.status(400).body("Error");

    }
}
