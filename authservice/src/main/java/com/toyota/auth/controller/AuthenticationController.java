package com.toyota.auth.controller;

import com.toyota.auth.dto.UserDto;
import com.toyota.auth.service.AuthenticationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/v1/login")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/save")
    public ResponseEntity<String> save (@RequestBody UserDto userDto){
        log.info("User save ! : (username) " + userDto.getUsername());
        String responseObject = authenticationService.save(userDto);
        log.info("User saved ! : (username) " + userDto.getUsername());
        return ResponseEntity.ok(responseObject);
    }


    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestBody UserDto userDto){
        log.info("User authentication ! : (username) " + userDto.getUsername());
        String responseObject = authenticationService.auth(userDto);
        log.info("User authenticated ! : (username) " + userDto.getUsername());
        return ResponseEntity.ok(responseObject);
    }

    @GetMapping("/getUser")
    public ResponseEntity<UserDto> getUserDtoByToken(@RequestParam String token){
        log.info("User get by token ! : (token) " + token);
        return ResponseEntity.ok(authenticationService.getUserDto(token));
    }

}
