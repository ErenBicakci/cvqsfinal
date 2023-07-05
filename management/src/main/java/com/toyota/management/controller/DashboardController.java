package com.toyota.management.controller;

import com.toyota.management.log.CustomLogInfo;
import com.toyota.management.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;

    @CustomLogInfo
    @GetMapping
    public ResponseEntity<String> getDashboard(){
        System.out.println(userService.getUser("eren"));
        return ResponseEntity.ok("Dashboard");
    }
}
