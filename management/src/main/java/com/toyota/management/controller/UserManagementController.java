package com.toyota.management.controller;

import com.toyota.management.dto.GetUserParameters;
import com.toyota.management.dto.UserDto;
import com.toyota.management.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/usermanagement")
@RequiredArgsConstructor
public class UserManagementController {
    private final UserManagementService userManagementService;

    @GetMapping("/user/showusers")
    private ResponseEntity<List<UserDto>> showUsers(@RequestParam String filterKeyword, @RequestParam int page, @RequestParam int pageSize, @RequestParam String sortType){
        GetUserParameters getUserParameters = GetUserParameters.builder()
                .filterKeyword(filterKeyword)
                .page(page)
                .pageSize(pageSize)
                .sortType(sortType)
                .build();
        return ResponseEntity.ok(userManagementService.getUsers(getUserParameters));
    }
    @DeleteMapping("/user/{username}")
    private ResponseEntity<Boolean> deleteUser(@PathVariable String username){
        boolean status = userManagementService.deleteUser(username);
        if (status){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(400).body(false);

    }
    @GetMapping("/user/{username}")
    private ResponseEntity<UserDto> getUser(@PathVariable String username){
        UserDto responseObject = userManagementService.getUser(username);
        return ResponseEntity.ok(responseObject);

    }

    @PutMapping("/user/updateuser")
    private ResponseEntity<UserDto> updateuser(@RequestBody UserDto nameSurnameDto){
        UserDto responseObject = userManagementService.updateUser(nameSurnameDto);
        return ResponseEntity.ok(responseObject);

    }

    @PostMapping("/role/{username}/{role}")
    private ResponseEntity<UserDto> userAddRole(@PathVariable String username,@PathVariable String role){
        UserDto responseObject = userManagementService.userAddRole(username,role);
        return ResponseEntity.ok(responseObject);
    }

    @DeleteMapping("/role/{username}/{role}")
    private ResponseEntity<Void> userDelRole(@PathVariable String username,@PathVariable String role) {
        boolean status = userManagementService.userDelRole(username, role);
        if (status) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(400).build();

    }
}
