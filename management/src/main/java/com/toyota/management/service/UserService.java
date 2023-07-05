package com.toyota.management.service;

import com.toyota.management.dto.GetUserParameters;
import com.toyota.management.dto.UserDto;
import com.toyota.management.entity.User;
import com.toyota.management.exception.UserNotFoundException;
import com.toyota.management.log.CustomLogDebug;
import com.toyota.management.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }




    /**
     *
     * Get Users with filter and sort parameters
     *
     * @param parameters - GetUserParameters  - page, pageSize, sortType, filterKeyword
     * @return List<UserDto> - UserDto List (id, nameSurname, username, roles)
     */
    @CustomLogDebug
    public List<UserDto> getUsers(GetUserParameters parameters){
        Sort sort;
        if (parameters.getSortType().equals("ASC")){
            sort = Sort.by(Sort.Direction.ASC, "id");
        }
        else {
            sort = Sort.by(Sort.Direction.DESC, "id");
        }
        Pageable pageable = PageRequest.of(parameters.getPage(), parameters.getPageSize(), sort);
        return userRepository.findAllByNameSurnameLikeAndDeletedFalse("%"+parameters.getFilterKeyword()+"%",pageable).get().toList().stream().map(user -> UserDto.builder().id(user.getId()).roles(user.getRoles()).nameSurname(user.getNameSurname()).username(user.getUsername()).build()).collect(Collectors.toList());

    }

    /**
     *
     * Delete User with username
     *
     * @param username - String  - username
     * @return if user deleted return true
     */
    @CustomLogDebug
    public boolean deleteUser(String username){

        User user = userRepository.findByUsernameAndDeletedFalse(username).orElseThrow(() -> new UserNotFoundException("User Not Found!"));

        user.setDeleted(true);
        userRepository.save(user);
        return true;

    }

    /**
     *
     * Get User with username
     *
     * @param username - String  - username
     * @return UserDto - UserDto (id, nameSurname, username, roles)
     */
    @CustomLogDebug
    public UserDto getUser(String username){

        User user = userRepository.findByUsernameAndDeletedFalse(username).orElseThrow(() -> new UserNotFoundException("User Not Found!"));
        return UserDto
                .builder()
                .id(user.getId())
                .roles(user.getRoles())
                .nameSurname(user.getNameSurname())
                .username(user.getUsername())
                .build();
    }


    /**
     *
     * Update User with UserDto
     *
     * @param nameSurnameDto - UserDto  - id, nameSurname, username, roles
     * @return UserDto - UserDto (id, nameSurname, username, roles)
     */
    @CustomLogDebug
    public UserDto updateUser(UserDto nameSurnameDto){

        User user = userRepository.findByUsernameAndDeletedFalse(nameSurnameDto.getUsername()).orElseThrow(() -> new UserNotFoundException("User Not Found!"));

        user.setNameSurname(nameSurnameDto.getNameSurname());
        userRepository.save(user);
        return UserDto.builder().roles(user.getRoles()).nameSurname(user.getNameSurname()).username(user.getUsername()).build();

    }
}
