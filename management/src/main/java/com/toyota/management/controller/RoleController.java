package com.toyota.management.controller;

import com.toyota.management.dto.UserDto;
import com.toyota.management.log.CustomLogInfo;
import com.toyota.management.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;



    @PostMapping("/{username}/{role}")
    private ResponseEntity<UserDto> userAddRole(@PathVariable String username,@PathVariable String role){
        UserDto responseObject = roleService.userAddRole(username,role);
        return ResponseEntity.ok(responseObject);
    }

    @DeleteMapping("/{username}/{role}")
    private ResponseEntity<Void> userDelRole(@PathVariable String username,@PathVariable String role) {
        boolean status = roleService.userDelRole(username, role);
        if (status) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(400).build();

    }
}
