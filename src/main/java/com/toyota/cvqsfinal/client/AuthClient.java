package com.toyota.cvqsfinal.client;

import com.toyota.cvqsfinal.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "auth",url = "http://auth:8181/api/login/")
public interface AuthClient {
    @PostMapping("/save")
    ResponseEntity<String> save(@RequestBody UserDto userDto);


    @PostMapping("/auth")
    ResponseEntity<String> auth(@RequestBody UserDto userDto);

    @PostMapping("/getUser")
    ResponseEntity<UserDto> getUserByToken(@RequestBody String token);

}