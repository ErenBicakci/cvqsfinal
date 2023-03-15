package com.toyota.management.service;

import com.toyota.management.dto.GetUserParameters;
import com.toyota.management.dto.UserDto;
import com.toyota.management.entity.Role;
import com.toyota.management.entity.User;
import com.toyota.management.exception.GenericException;
import com.toyota.management.exception.UserNotFoundException;
import com.toyota.management.repository.RoleRepository;
import com.toyota.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserManagementService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;



    public List<UserDto> getUsers(GetUserParameters parameters){

        Sort sort;
        if (parameters.getSortType().equals("ASC")){
            sort = Sort.by(Sort.Direction.ASC, "id");
        }
        else {
            sort = Sort.by(Sort.Direction.DESC, "id");
        }
        Pageable pageable = PageRequest.of(parameters.getPage(), parameters.getPageSize(), sort);
        return userRepository.findAllByNameSurnameLikeAndDeletedFalse("%"+parameters.getFilterKeyword()+"%",pageable).get().collect(Collectors.toList()).stream().map(user -> UserDto.builder().id(user.getId()).roles(user.getRoles()).nameSurname(user.getNameSurname()).username(user.getUsername()).build()).collect(Collectors.toList());

    }

    public boolean deleteUser(String username){

        User user = userRepository.findByUsernameAndDeletedFalse(username);
        if (user == null && user.isDeleted()){
            throw new UserNotFoundException("User Not Found!");
        }
        user.setDeleted(true);
        userRepository.save(user);
        return true;

    }

    public UserDto getUser(String username){

        User user = userRepository.findByUsernameAndDeletedFalse(username);
        if (user != null){
            return UserDto.builder().id(user.getId()).roles(user.getRoles()).nameSurname(user.getNameSurname()).username(user.getUsername()).build();
        }
        throw new UserNotFoundException("User Not Found!");
    }

    public UserDto updateUser(UserDto nameSurnameDto){

        User user = userRepository.findByUsernameAndDeletedFalse(nameSurnameDto.getUsername());
        if (user != null){
            user.setNameSurname(nameSurnameDto.getNameSurname());
            userRepository.save(user);
            return UserDto.builder().roles(user.getRoles()).nameSurname(user.getNameSurname()).username(user.getUsername()).build();
        }
        throw new UserNotFoundException("User Not Found!");
    }

    public UserDto userAddRole(String username,String role){
        if (roleRepository.findByName(role) == null){
            throw new GenericException("Role not available!");
        }
        User user = userRepository.findByUsernameAndDeletedFalse(username);
        if (user == null)
        {
            log.debug("Username Not Found!");
            throw new UserNotFoundException("User Not Found!");
        }
        List<Role> roles = new ArrayList<>();
        for (int i = 0;i < user.getRoles().size();i++){
            if (role.equals(user.getRoles().get(i).getName())){
                throw new GenericException("Role already exists!");
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

    public boolean userDelRole(String username,String role){
        if (roleRepository.findByName(role) == null){
            log.info("Role not available!");
            throw new GenericException("Role not available!");
        }

        User user = userRepository.findByUsernameAndDeletedFalse(username);

        if (user == null){
            log.info("Username Not Found!");
            throw new UserNotFoundException("User Not Found!");
        }

        List<Role> roles = new ArrayList<>();
        int counter = 0;
        for (int i = 0;i < user.getRoles().size();i++){
            if (role.equals(user.getRoles().get(i).getName())){
                if (user.getRoles().size() == 1){
                    log.info("User must have at least 1 role");
                    throw  new GenericException("User must have at least 1 role");
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
            throw  new GenericException("User does not have this role");

        }
        user.setRoles(roles);
        userRepository.save(user);
        log.debug("Role Deleted");
        return true;
    }
}