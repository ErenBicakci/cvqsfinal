package com.toyota.auth.service;


import com.toyota.auth.dto.UserDto;
import com.toyota.auth.entity.User;
import com.toyota.auth.repository.RoleRepository;
import com.toyota.auth.repository.UserRepositoryStub;
import com.toyota.auth.utility.StringManipulation;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Spy
    private UserRepositoryStub userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private StringManipulation stringManipulation;

    @Spy
    private JwtService jwtService;

    @Test
    void testSave() {
        String token = authenticationService.save(UserDto.builder().nameSurname("test").password("test").username("TestUsername").build());
        Assert.assertEquals(jwtService.findUsername(token), "TestUsername");
    }

    @Test
    void testAuth() {
        String token = authenticationService.auth(UserDto.builder().nameSurname("test").password("test").username("TestUsername2").build());

        Assert.assertEquals(jwtService.findUsername(token), "TestUsername2");
    }




}