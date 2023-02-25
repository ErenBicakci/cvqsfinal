package com.toyota.cvqsfinal.repository;

import com.toyota.cvqsfinal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    User findByUsernameAndDeletedFalse(String username);

    Optional<User> findUserByUsernameAndDeletedFalse(String username);
    List<User> findAllByDeletedFalse();
}
