package com.toyota.management.service;

import com.toyota.management.dto.GetUserParameters;
import com.toyota.management.dto.UserDto;
import com.toyota.management.entity.User;
import com.toyota.management.exception.UserNotFoundException;
import com.toyota.management.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;



    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    void testGetUsers() {
        GetUserParameters parameters = GetUserParameters.builder().page(0).pageSize(10).sortType("ASC").filterKeyword("").build();

        Pageable pageable = PageRequest.of(parameters.getPage(), parameters.getPageSize(), Sort.by(Sort.Direction.ASC, "id"));
        Mockito.when(userRepository.findAllByNameSurnameLikeAndDeletedFalse("%"+parameters.getFilterKeyword()+"%",pageable)).thenReturn(null);
        try {
            userService.getUsers(parameters);
        } catch (Exception e) {
        }
        Mockito.verify(userRepository).findAllByNameSurnameLikeAndDeletedFalse("%"+parameters.getFilterKeyword()+"%",pageable);

    }

    @Test
    void testDeleteUser() {
        User user = User.builder().id(1L).username("test").deleted(false).build();
        Mockito.when(userRepository.findByUsernameAndDeletedFalse("test")).thenReturn(Optional.of(user));
        boolean result = userService.deleteUser("test");
        assertTrue(result);
        Mockito.verify(userRepository).save(user);
        assertTrue(user.isDeleted());
    }

    @Test
    void testDeleteUserButUserNotFound() {
        Mockito.when(userRepository.findByUsernameAndDeletedFalse("test")).thenReturn(Optional.empty());
        try {
            userService.deleteUser("test");
        } catch (Exception e) {
            assertEquals(e.getClass(), UserNotFoundException.class);
        }
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }


    @Test
    void testGetUser() {
        User user = User.builder().id(1L).username("test").deleted(false).build();
        Mockito.when(userRepository.findByUsernameAndDeletedFalse("test")).thenReturn(Optional.of(user));
        UserDto result = userService.getUser("test");
        Mockito.verify(userRepository).findByUsernameAndDeletedFalse("test");
        assertEquals(result.getUsername(), user.getUsername());
    }

    @Test
    void testGetUserButUserNotFound(){
        Mockito.when(userRepository.findByUsernameAndDeletedFalse("test")).thenReturn(Optional.empty());
        try {
            userService.getUser("test");
        } catch (Exception e) {
            assertEquals(e.getClass(), UserNotFoundException.class);
        }
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testUpdateUser() {
        User user = User.builder().id(1L).username("test").deleted(false).build();
        Mockito.when(userRepository.findByUsernameAndDeletedFalse("test")).thenReturn(Optional.of(user));
        UserDto result = userService.updateUser(UserDto.builder().username("test").nameSurname("test name surname").build());
        assertEquals(result.getNameSurname(), "test name surname");
        Mockito.verify(userRepository).save(user);
    }
}
