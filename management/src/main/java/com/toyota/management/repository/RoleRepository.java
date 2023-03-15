package com.toyota.management.repository;

import com.toyota.management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    List<Role> findAllByName(String name);
    Role findByName(String name);


}