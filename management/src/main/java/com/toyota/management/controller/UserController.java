package com.toyota.management.controller;

import com.toyota.management.dto.GetUserParameters;
import com.toyota.management.dto.UserDto;
import com.toyota.management.log.CustomLogInfo;
import com.toyota.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    @GetMapping("/showusers")
    private ResponseEntity<List<UserDto>> showUsers(@RequestParam String filterKeyword, @RequestParam int page, @RequestParam int pageSize, @RequestParam String sortType){
        GetUserParameters getUserParameters = GetUserParameters.builder()
                .filterKeyword(filterKeyword)
                .page(page)
                .pageSize(pageSize)
                .sortType(sortType)
                .build();
        return ResponseEntity.ok(userService.getUsers(getUserParameters));
    }


    @DeleteMapping("/{username}")
    private ResponseEntity<Boolean> deleteUser(@PathVariable String username){
        boolean status = userService.deleteUser(username);
        if (status){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(400).body(false);

    }


    @GetMapping("/{username}")
    private ResponseEntity<UserDto> getUser(@PathVariable String username){
        UserDto responseObject = userService.getUser(username);
        return ResponseEntity.ok(responseObject);

    }



    @PutMapping
    private ResponseEntity<UserDto> updateuser(@RequestBody UserDto nameSurnameDto){
        UserDto responseObject = userService.updateUser(nameSurnameDto);
        return ResponseEntity.ok(responseObject);

    }
}

