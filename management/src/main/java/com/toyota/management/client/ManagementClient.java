package com.toyota.management.client;

import com.toyota.management.dto.TokenControl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "auth",url = "http://auth:8181/api/v1/auth")

public interface ManagementClient {
    @GetMapping("/validate")
    TokenControl getAuthorities(@RequestParam String jwtToken);



}
