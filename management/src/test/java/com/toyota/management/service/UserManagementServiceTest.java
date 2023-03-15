package com.toyota.management.service;

import com.toyota.management.dto.UserDto;
import com.toyota.management.repository.RoleRepository;
import com.toyota.management.repository.UserRepositoryStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.Assert;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)

public class UserManagementServiceTest {
    @InjectMocks
    private UserManagementService userManagementService;
    @Spy
    private UserRepositoryStub userRepositoryStub;
    @Mock
    private RoleRepository roleRepositoryStub;

    @Test
    public void testGetUser(){
        Assert.assertEquals("TestUsername", userManagementService.getUser("TestUsername").getUsername());
    }

    @Test
    public void testUpdateUser(){
        Assert.assertEquals(userManagementService.updateUser(UserDto.builder().username("TestUsername").nameSurname("test2").build()).getNameSurname(),"test2");
    }

    @Test
    public void testDeleteUser(){
        Assert.assertEquals(userManagementService.deleteUser("TestUsername"),true);
    }
}
