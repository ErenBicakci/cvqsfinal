package com.toyota.cvqsfinal.dto;

import com.toyota.cvqsfinal.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenControl {

    private String username;

    private List<Role> roles;
    boolean status;
}
