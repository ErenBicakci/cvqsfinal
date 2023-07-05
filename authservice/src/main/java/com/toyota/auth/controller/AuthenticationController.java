package com.toyota.auth.controller;

import com.toyota.auth.dto.UserDto;
import com.toyota.auth.log.CustomLogInfo;
import com.toyota.auth.service.AuthenticationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @CustomLogInfo
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto){
        return ResponseEntity.ok(authenticationService.registerUser(userDto));
    }


    @CustomLogInfo
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDto userDto){
        return ResponseEntity.ok(authenticationService.auth(userDto));
    }

    @CustomLogInfo
    @GetMapping("/getroles")
    public List<String> getRoles(@RequestParam String username){
        return authenticationService.getUserRoles(username);
    }


}
