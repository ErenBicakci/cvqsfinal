package com.toyota.auth.dto;

import com.toyota.auth.entity.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class UserDto {
    private Long id;
    private String nameSurname;
    private String username;
    private String password;

    private List<Role> roles;

}
