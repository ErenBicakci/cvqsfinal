package com.toyota.cvqsfinal.controller;

import com.toyota.cvqsfinal.dto.UserDto;
import com.toyota.cvqsfinal.entity.User;
import com.toyota.cvqsfinal.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usermanagement")
@RequiredArgsConstructor
public class UserManagementController {
    private final UserManagementService userManagementService;

    @GetMapping("/showusers")
    private ResponseEntity<List<UserDto>> showUsers(){

        return ResponseEntity.ok(userManagementService.getUsers());
    }

    @DeleteMapping("/deleteuser/{username}")
    private void deleteUser(@PathVariable String username, @RequestHeader(name="Authorization") String token){
        userManagementService.deleteUser(username,token.substring(7));

    }

    @GetMapping("/getuser/{username}")
    private ResponseEntity<UserDto> getUser(@PathVariable String username){

        UserDto responseObject = userManagementService.getUser(username);
        if (responseObject == null){
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.ok(responseObject);

    }

    @PutMapping("/updatenamesurname")
    private ResponseEntity<UserDto> updateNameSurname(@RequestBody UserDto nameSurnameDto){
        UserDto responseObject = userManagementService.updateNameSurname(nameSurnameDto);
        if (responseObject == null){
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.ok(responseObject);

    }

    @PostMapping("/addrole/{username}/{role}")
    private ResponseEntity<UserDto> userAddRole(@PathVariable String username,@PathVariable String role){
        UserDto responseObject = userManagementService.userAddRole(username,role);
        if (responseObject == null){
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.ok(responseObject);
    }

    @DeleteMapping("/delrole/{username}/{role}")
    private void userDelRole(@PathVariable String username,@PathVariable String role){
        userManagementService.userDelRole(username,role);
    }
}
