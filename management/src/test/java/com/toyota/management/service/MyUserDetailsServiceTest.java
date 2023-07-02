package com.toyota.management.service;

import com.toyota.management.client.AuthClient;
import com.toyota.management.dto.UserDto;
import com.toyota.management.entity.Role;
import com.toyota.management.repository.RoleRepository;
import com.toyota.management.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyUserDetailsServiceTest {
    @Mock
    private AuthClient authClient;



    private MyUserDetailsService myUserDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        myUserDetailsService = new MyUserDetailsService(authClient);
    }


    @Test
    void testLoadUserByUsername() {
        List<Role> roles = List.of(Role.builder().name("ADMIN").build());
        UserDto userDto = UserDto
                .builder()
                .username("test")
                .password("test")
                .nameSurname("test")
                .roles(roles)
                .build();
        Mockito.when(authClient.getUserByToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlcmVuYmljMiIsImlhdCI6MTY4ODI2NzIzMSwiZXhwIjoxNjg4NDExMjMxfQ.LrCxVrQui33K2a4NAUfyRWHtg_ZWTJTMrGkjVYuc6ws")).thenReturn(ResponseEntity.ok(userDto));

        UserDetails userDetails = myUserDetailsService.loadUserByUsername("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlcmVuYmljMiIsImlhdCI6MTY4ODI2NzIzMSwiZXhwIjoxNjg4NDExMjMxfQ.LrCxVrQui33K2a4NAUfyRWHtg_ZWTJTMrGkjVYuc6ws");
        assertEquals("test", userDetails.getUsername());
        assertEquals("test", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("ADMIN", userDetails.getAuthorities().toArray()[0].toString());
    }
}