package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.UserDto;
import com.toyota.cvqsfinal.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;


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
