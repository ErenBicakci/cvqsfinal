package com.toyota.management.service;

import com.toyota.management.dto.UserDto;
import com.toyota.management.entity.Role;
import com.toyota.management.entity.User;
import com.toyota.management.exception.GenericException;
import com.toyota.management.exception.UserNotFoundException;
import com.toyota.management.log.CustomLogDebug;
import com.toyota.management.repository.RoleRepository;
import com.toyota.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class RoleService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;





    /**
     *
     * add Role to User with username and role
     *
     * @param username - String  - username
     * @param role - String  - role
     * @return UserDto - UserDto (id, nameSurname, username, roles)
     */
    @CustomLogDebug
    public UserDto userAddRole(String username,String role){
        Role roleObject = roleRepository.findByName(role).orElseThrow(() -> new GenericException("Role not available!"));

        User user = userRepository.findByUsernameAndDeletedFalse(username).orElseThrow(() -> new UserNotFoundException("User Not Found!"));
        List<Role> roles = new ArrayList<>();
        for (int i = 0;i < user.getRoles().size();i++){
            if (role.equals(user.getRoles().get(i).getName())){
                throw new GenericException("Role already exists!");
            }
            else {
                roles.add(user.getRoles().get(i));
            }
        }



        roles.add(roleObject);
        user.setRoles(roles);
        userRepository.save(user);
        return UserDto.builder().roles(user.getRoles()).nameSurname(user.getNameSurname()).username(user.getUsername()).build();
    }



    /**
     *
     * delete Role to User with username and role
     *
     * @param username - String  - username
     * @param role - String  - role
     * @return if user deleted return true
     */
    @CustomLogDebug
    public boolean userDelRole(String username,String role){

        roleRepository.findByName(role).orElseThrow(() -> new GenericException("Role not available!"));

        User user = userRepository.findByUsernameAndDeletedFalse(username).orElseThrow(() -> new UserNotFoundException("User Not Found!"));


        List<Role> roles = new ArrayList<>();
        int counter = 0;
        for (int i = 0;i < user.getRoles().size();i++){
            if (role.equals(user.getRoles().get(i).getName())){
                if (user.getRoles().size() == 1){
                    throw  new GenericException("User must have at least 1 role");
                }
                else {
                    counter++;
                }
            }
            else {
                roles.add(user.getRoles().get(i));
            }
        }
        if (counter ==0){
            throw  new GenericException("User does not have this role");

        }
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }
}
