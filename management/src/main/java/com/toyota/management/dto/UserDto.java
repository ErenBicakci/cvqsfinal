package com.toyota.management.dto;

import com.toyota.management.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String nameSurname;
    private String username;
    private String password;

    private List<Role> roles;
}
