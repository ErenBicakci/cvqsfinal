package com.toyota.auth.controller;

import com.toyota.auth.dto.UserDto;
import com.toyota.auth.log.CustomLogInfo;
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


    @CustomLogInfo
    @PostMapping("/save")
    public ResponseEntity<String> save (@RequestBody UserDto userDto){
        String responseObject = authenticationService.save(userDto);
        return ResponseEntity.ok(responseObject);
    }


    @CustomLogInfo
    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestBody UserDto userDto){
        String responseObject = authenticationService.auth(userDto);
        return ResponseEntity.ok(responseObject);
    }

    @CustomLogInfo
    @GetMapping("/getUser")
    public ResponseEntity<UserDto> getUserDtoByToken(@RequestParam String token){
        return ResponseEntity.ok(authenticationService.getUserDto(token));
    }

}
