package com.toyota.management.controller;

import com.toyota.management.dto.GetUserParameters;
import com.toyota.management.dto.UserDto;
import com.toyota.management.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        GetUserParameters getUserParameters = GetUserParameters.builder()
                .filterKeyword(filterKeyword)
                .page(page)
                .pageSize(pageSize)
                .sortType(sortType)
                .build();
        log.info(auth.getName() + "Send show users request");
        return ResponseEntity.ok(userManagementService.getUsers(getUserParameters));
    }

    @DeleteMapping("/user/{username}")
    private ResponseEntity<Void> deleteUser(@PathVariable String username){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(auth.getName() + "Send delete user request : (username) " + username);
        boolean status = userManagementService.deleteUser(username);
        log.info(auth.getName() + "User Deleted ! : (username) " + username);
        return ResponseEntity.ok().build();


    }

    @GetMapping("/user/{username}")
    private ResponseEntity<UserDto> getUser(@PathVariable String username){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(auth.getName() + "Send get user request : (username) " + username);

        UserDto responseObject = userManagementService.getUser(username);
        log.info(auth.getName() + "User Found ! : (username) " + username);
        return ResponseEntity.ok(responseObject);

    }

    @PutMapping("/user/updateuser")
    private ResponseEntity<UserDto> updateuser(@RequestBody UserDto nameSurnameDto){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info(auth.getName() + "Send update user request : (username) " + nameSurnameDto.getUsername());
        UserDto responseObject = userManagementService.updateUser(nameSurnameDto);
        log.info(auth.getName() + "User Updated ! : (username) " + nameSurnameDto.getUsername());
        return ResponseEntity.ok(responseObject);

    }

    @PostMapping("/role/{username}/{role}")
    private ResponseEntity<UserDto> userAddRole(@PathVariable String username,@PathVariable String role){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(auth.getName() + "Send add role request : (username) " + username + " (role) " + role);
        UserDto responseObject = userManagementService.userAddRole(username,role);
        log.info(auth.getName() + "Role Added ! : (username) " + username + " (role) " + role);
        return ResponseEntity.ok(responseObject);
    }

    @DeleteMapping("/role/{username}/{role}")
    private ResponseEntity<Void> userDelRole(@PathVariable String username,@PathVariable String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(auth.getName() + "Send delete role request : (username) " + username + " (role) " + role);
        boolean status = userManagementService.userDelRole(username, role);
        log.info(auth.getName() + "Role Deleted ! : (username) " + username + " (role) " + role);
        return ResponseEntity.ok().build();
    }
}
