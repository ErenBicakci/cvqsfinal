package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.UserDto;
import com.toyota.cvqsfinal.service.AuthenticationService;
import com.toyota.cvqsfinal.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;


    @PostMapping("/save")
    public ResponseEntity<String> save (@RequestBody UserDto userDto){
        String responseObject = authenticationService.save(userDto);
        if (responseObject == null){
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.ok(responseObject);
    }


    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestBody UserDto userDto){
        String responseObject = authenticationService.auth(userDto);
        if (responseObject == null){
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.ok(responseObject);
    }

    @GetMapping
    public ResponseEntity<String> helloWorld(){
        return ResponseEntity.ok("Merhaba Dünya!");
    }
}
