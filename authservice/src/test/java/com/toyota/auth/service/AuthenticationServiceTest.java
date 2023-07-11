package com.toyota.auth.service;
import com.toyota.auth.dto.UserDto;
import com.toyota.auth.entity.Role;
import com.toyota.auth.entity.User;
import com.toyota.auth.jwt.JwtService;
import com.toyota.auth.repository.RoleRepository;
import com.toyota.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Spy
    private JwtService jwtService;
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(userRepository, jwtService, authenticationManager, roleRepository);
    }
    @Test
    public void testRegisterUser() {
        UserDto userDto = UserDto.builder().username("testuser").password("testpassword").nameSurname("Test User").build();
        String token = authenticationService.registerUser(userDto);
        assertEquals(jwtService.findUsername(token), userDto.getUsername());
        Mockito.verify(userRepository).save(ArgumentMatchers.any(User.class));

    }

    @Test
    public void testAuth(){
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().name("ROLE_ADMIN").build());
        User user = User.builder().username("testuser").password("testpassword").roles(roles).nameSurname("Test User").build();
        Mockito.when(userRepository.findByUsernameAndDeletedFalse("testuser")).thenReturn(Optional.of(user));
        Mockito.when(authenticationManager.authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        UserDto userDto = UserDto.builder().username("testuser").password("testpassword").nameSurname("Test User").build();
        String token = authenticationService.auth(userDto);
        assertEquals(jwtService.findUsername(token), userDto.getUsername());
    }

    @Test
    public void testGetRoles(){

        List<Role> roles = List.of(Role.builder().name("ROLE_ADMIN").build(), Role.builder().name("ROLE_USER").build(), Role.builder().name("ROLE_TEAMLEADER").build());
        User user = User.builder().username("testuser").password("testpassword").nameSurname("Test User").roles(roles).build();
        Mockito.when(userRepository.findByUsernameAndDeletedFalse("testuser")).thenReturn(Optional.of(user));
        List<String> userRoles = authenticationService.getUserRoles(user.getUsername());
        assertEquals(roles.get(0).getName(), userRoles.get(0));
        assertEquals(roles.get(1).getName(), userRoles.get(1));
        assertEquals(roles.get(2).getName(), userRoles.get(2));
    }

}