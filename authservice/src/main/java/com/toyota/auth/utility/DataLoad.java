package com.toyota.auth.utility;

import com.toyota.auth.entity.Role;
import com.toyota.auth.repository.RoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoad implements ApplicationRunner {

     private final RoleRepository roleRepository;

    public DataLoad(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    //if role table is empty, create required roles
    @Override
    public void run(ApplicationArguments args) {
        //Role Save

        if (roleRepository.findAll().size() == 0){
            Role roleOperator = Role.builder().name("ROLE_OPERATOR").build();
            Role roleTeamLeader = Role.builder().name("ROLE_TEAMLEADER").build();
            Role roleAdmin = Role.builder().name("ROLE_ADMIN").build();

            roleRepository.save(roleAdmin);
            roleRepository.save(roleTeamLeader);
            roleRepository.save(roleOperator);
        }


    }
}
