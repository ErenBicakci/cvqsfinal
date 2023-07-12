package com.toyota.cvqsfinal.client;

import com.toyota.cvqsfinal.dto.TokenControl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "auth",url = "http://auth:8181/api/v1/auth")

public interface ManagementClient {
    @GetMapping("/validate")
    TokenControl getAuthorities(@RequestParam String jwtToken);

}
