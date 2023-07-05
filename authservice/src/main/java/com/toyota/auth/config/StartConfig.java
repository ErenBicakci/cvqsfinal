package com.toyota.auth.config;

import com.toyota.auth.entity.Role;
import com.toyota.auth.repository.RoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartConfig implements ApplicationRunner {

    private final RoleRepository roleRepository;

    public StartConfig(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
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
