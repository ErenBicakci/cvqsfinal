package com.toyota.cvqsfinal.utility;

import com.toyota.cvqsfinal.entity.Role;
import com.toyota.cvqsfinal.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataLoad implements ApplicationRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //Role Save

        if (roleRepository.findAll().size() == 0){
            Role roleOperator = Role.builder().id(1L).name("ROLE_OPERATOR").build();
            Role roleTeamLeader = Role.builder().id(2L).name("ROLE_TEAMLEADER").build();
            Role roleAdmin = Role.builder().id(3L).name("ROLE_ADMIN").build();

            roleRepository.save(roleAdmin);
            roleRepository.save(roleTeamLeader);
            roleRepository.save(roleOperator);
        }


    }
}
