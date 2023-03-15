package com.toyota.management.client;

import com.toyota.management.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "auth",url = "http://auth:8181/api/v1/login/")
public interface AuthClient {
    @PostMapping("/save")
    ResponseEntity<String> save(@RequestBody UserDto userDto);


    @PostMapping("/auth")
    ResponseEntity<String> auth(@RequestBody UserDto userDto);

    @GetMapping("/getUser")
    ResponseEntity<UserDto> getUserByToken(@RequestParam String token);

}