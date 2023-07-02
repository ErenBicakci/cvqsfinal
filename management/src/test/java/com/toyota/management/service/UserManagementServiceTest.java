package com.toyota.management.service;

import com.toyota.management.dto.GetUserParameters;
import com.toyota.management.dto.UserDto;
import com.toyota.management.entity.Role;
import com.toyota.management.entity.User;
import com.toyota.management.exception.GenericException;
import com.toyota.management.exception.UserNotFoundException;
import com.toyota.management.repository.RoleRepository;
import com.toyota.management.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserManagementServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;


    private UserManagementService userManagementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userManagementService = new UserManagementService(userRepository, roleRepository);
    }

    @Test
    void testGetUsers() {
        GetUserParameters parameters = GetUserParameters.builder().page(0).pageSize(10).sortType("ASC").filterKeyword("").build();

        Pageable pageable = PageRequest.of(parameters.getPage(), parameters.getPageSize(), Sort.by(Sort.Direction.ASC, "id"));
        Mockito.when(userRepository.findAllByNameSurnameLikeAndDeletedFalse("%"+parameters.getFilterKeyword()+"%",pageable)).thenReturn(null);
        try {
            userManagementService.getUsers(parameters);
        } catch (Exception e) {
        }
        Mockito.verify(userRepository).findAllByNameSurnameLikeAndDeletedFalse("%"+parameters.getFilterKeyword()+"%",pageable);

    }

    @Test
    void testDeleteUser() {
        User user = User.builder().id(1L).username("test").deleted(false).build();
        Mockito.when(userRepository.findByUsernameAndDeletedFalse("test")).thenReturn(Optional.of(user));
        boolean result = userManagementService.deleteUser("test");
        assertTrue(result);
        Mockito.verify(userRepository).save(user);
        assertTrue(user.isDeleted());
    }

    @Test
    void testDeleteUserButUserNotFound() {
        Mockito.when(userRepository.findByUsernameAndDeletedFalse("test")).thenReturn(Optional.empty());
        try {
            userManagementService.deleteUser("test");
        } catch (Exception e) {
            assertEquals(e.getClass(), UserNotFoundException.class);
        }
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }


    @Test
    void testGetUser() {
        User user = User.builder().id(1L).username("test").deleted(false).build();
        Mockito.when(userRepository.findByUsernameAndDeletedFalse("test")).thenReturn(Optional.of(user));
        UserDto result = userManagementService.getUser("test");
        Mockito.verify(userRepository).findByUsernameAndDeletedFalse("test");
        assertEquals(result.getUsername(), user.getUsername());
    }

    @Test
    void testGetUserButUserNotFound(){
        Mockito.when(userRepository.findByUsernameAndDeletedFalse("test")).thenReturn(Optional.empty());
        try {
            userManagementService.getUser("test");
        } catch (Exception e) {
            assertEquals(e.getClass(), UserNotFoundException.class);
        }
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testUpdateUser() {
        User user = User.builder().id(1L).username("test").deleted(false).build();
        Mockito.when(userRepository.findByUsernameAndDeletedFalse("test")).thenReturn(Optional.of(user));
        UserDto result = userManagementService.updateUser(UserDto.builder().username("test").nameSurname("test name surname").build());
        assertEquals(result.getNameSurname(), "test name surname");
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void testUserAddRole() {
        Role role = Role.builder().id(1L).name("ROLE_ADMIN").build();
        Role role2 = Role.builder().id(2L).name("ROLE_OPERATOR").build();
        Role role3 = Role.builder().id(3L).name("ROLE_TEAMLEADER").build();
        Mockito.when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(role));
        Mockito.when(roleRepository.findByName("ROLE_OPERATOR")).thenReturn(Optional.of(role2));
        Mockito.when(roleRepository.findByName("ROLE_TEAMLEADER")).thenReturn(Optional.of(role3));

        //user have only admin role
        List<Role> roleList = List.of(role);
        User user = User.builder().id(1L).username("test").deleted(false).roles(roleList).build();
        Mockito.when(userRepository.findByUsernameAndDeletedFalse("test")).thenReturn(Optional.of(user));
        UserDto result = userManagementService.userAddRole("test", "ROLE_TEAMLEADER");
        assertEquals(result.getRoles().get(1).getName(), "ROLE_TEAMLEADER");
    }

    @Test
    void testUserAddRoleButUserHaveAllRoles(){
        Role role = Role.builder().id(1L).name("ROLE_ADMIN").build();
        Role role2 = Role.builder().id(2L).name("ROLE_OPERATOR").build();
        Role role3 = Role.builder().id(3L).name("ROLE_TEAMLEADER").build();
        Mockito.when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(role));
        Mockito.when(roleRepository.findByName("ROLE_OPERATOR")).thenReturn(Optional.of(role2));
        Mockito.when(roleRepository.findByName("ROLE_TEAMLEADER")).thenReturn(Optional.of(role3));

        List<Role> roleList = List.of(role, role2, role3);
        User user = User.builder().id(1L).username("test").deleted(false).roles(roleList).build();
        Mockito.when(userRepository.findByUsernameAndDeletedFalse("test")).thenReturn(Optional.of(user));
        try {
            userManagementService.userAddRole("test", "ROLE_TEAMLEADER");
        } catch (Exception e) {
            assertEquals(e.getClass(), GenericException.class);
            assertEquals(e.getMessage(), "Role already exists!");
        }
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testUserDelRole() {
        Role role = Role.builder().id(1L).name("ROLE_ADMIN").build();
        Role role2 = Role.builder().id(2L).name("ROLE_OPERATOR").build();
        Role role3 = Role.builder().id(3L).name("ROLE_TEAMLEADER").build();
        Mockito.when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(role));
        Mockito.when(roleRepository.findByName("ROLE_OPERATOR")).thenReturn(Optional.of(role2));
        Mockito.when(roleRepository.findByName("ROLE_TEAMLEADER")).thenReturn(Optional.of(role3));

        List<Role> roleList = List.of(role, role3);
        User user = User.builder().id(1L).username("test").deleted(false).roles(roleList).build();
        Mockito.when(userRepository.findByUsernameAndDeletedFalse("test")).thenReturn(Optional.of(user));
        assertTrue(userManagementService.userDelRole("test", "ROLE_TEAMLEADER"));

    }
}