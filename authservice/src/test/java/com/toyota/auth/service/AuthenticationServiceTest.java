package com.toyota.auth.service;
import com.toyota.auth.dto.UserDto;
import com.toyota.auth.entity.User;
import com.toyota.auth.repository.RoleRepository;
import com.toyota.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

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
    public void testSave() {
        UserDto userDto = UserDto.builder().username("testuser").password("testpassword").nameSurname("Test User").build();
        String token = authenticationService.save(userDto);
        assertEquals(jwtService.findUsername(token), userDto.getUsername());
        Mockito.verify(userRepository).save(ArgumentMatchers.any(User.class));

    }

    @Test
    public void testAuth(){
        User user = User.builder().username("testuser").password("testpassword").nameSurname("Test User").build();
        Mockito.when(userRepository.findByUsernameAndDeletedFalse("testuser")).thenReturn(user);
        Mockito.when(authenticationManager.authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        UserDto userDto = UserDto.builder().username("testuser").password("testpassword").nameSurname("Test User").build();
        String token = authenticationService.auth(userDto);
        assertEquals(jwtService.findUsername(token), userDto.getUsername());
    }
    @Test
    public void testGetUserDto(){
        User user = User.builder().username("testuser").password("testpassword").nameSurname("Test User").build();
        Mockito.when(userRepository.findByUsernameAndDeletedFalse("testuser")).thenReturn(user);

        UserDto userDto = UserDto.builder().username("testuser").password("testpassword").nameSurname("Test User").build();
        String token = authenticationService.auth(userDto);
        UserDto userDto2 = authenticationService.getUserDto(token);

        assertEquals(userDto2.getUsername(), user.getUsername());
        assertEquals(userDto2.getNameSurname(), user.getNameSurname());
    }
}