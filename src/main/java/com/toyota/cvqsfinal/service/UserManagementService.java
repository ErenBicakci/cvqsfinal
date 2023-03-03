package com.toyota.cvqsfinal.service;

import com.toyota.cvqsfinal.dto.UserDto;
import com.toyota.cvqsfinal.entity.Role;
import com.toyota.cvqsfinal.entity.User;
import com.toyota.cvqsfinal.repository.RoleRepository;
import com.toyota.cvqsfinal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserManagementService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;



    public List<UserDto> getUsers(){
        List<UserDto> users = new ArrayList<>();
        userRepository.findAllByDeletedFalse().stream()
                .forEach(user -> users.add(UserDto
                        .builder()
                        .username(user.getUsername())
                        .nameSurname(user.getNameSurname())
                        .roles(user.getRoles())
                        .build()));
        return users;
    }

    public void deleteUser(String username){
        try {
            User user = userRepository.findByUsernameAndDeletedFalse(username);
            if (user == null){
                return;
            }
            if (user.isDeleted()){
                return;
            }
            user.setDeleted(true);
            userRepository.save(user);

        }
        catch (Exception e){

        }


    }

    public UserDto getUser(String username){

        User user = userRepository.findByUsernameAndDeletedFalse(username);
        if (user != null){
            return UserDto.builder().roles(user.getRoles()).nameSurname(user.getNameSurname()).username(user.getUsername()).build();

        }
        log.info(username + " Not Found!");
        return null;
    }

    public UserDto updateNameSurname(UserDto nameSurnameDto){

        User user = userRepository.findByUsernameAndDeletedFalse(nameSurnameDto.getUsername());
        if (user != null){
            user.setNameSurname(nameSurnameDto.getNameSurname());
            userRepository.save(user);
            return UserDto.builder().roles(user.getRoles()).nameSurname(user.getNameSurname()).username(user.getUsername()).build();
        }
        log.info(nameSurnameDto.getUsername() + " Not Found !");
        return null;
    }

    public UserDto userAddRole(String username,String role){
        if (roleRepository.findByName(role) == null){
            return null;
        }
        User user = userRepository.findByUsernameAndDeletedFalse(username);
        if (user == null)
        {
            log.debug("Username Not Found!");
            return null;
        }
        List<Role> roles = new ArrayList<>();
        for (int i = 0;i < user.getRoles().size();i++){
            if (role.equals(user.getRoles().get(i).getName())){
                return null;
            }
            else {
                roles.add(user.getRoles().get(i));
            }
        }

        roles.add(roleRepository.findByName(role));
        user.setRoles(roles);
        userRepository.save(user);
        return UserDto.builder().roles(user.getRoles()).nameSurname(user.getNameSurname()).username(user.getUsername()).build();
    }

    public void userDelRole(String username,String role){
        if (roleRepository.findByName(role) == null){
            log.debug("Role not available!");
            return;
        }

        User user = userRepository.findByUsernameAndDeletedFalse(username);

        if (user == null){
            log.debug("Username Not Found!");
            return;
        }

        List<Role> roles = new ArrayList<>();
        int counter = 0;
        for (int i = 0;i < user.getRoles().size();i++){
            if (role.equals(user.getRoles().get(i).getName())){
                if (user.getRoles().size() == 1){
                    log.info("User must have at least 1 role");
                    return;
                }
                else {
                    counter++;
                    continue;
                }
            }
            else {
                roles.add(user.getRoles().get(i));
            }
        }
        if (counter ==0){
            log.debug("User does not have this role");
            return;
        }
        user.setRoles(roles);
        userRepository.save(user);
        log.debug("Role Deleted");
    }
}
