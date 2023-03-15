package com.toyota.management.repository;

import com.toyota.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    User findByUsernameAndDeletedFalse(String username);

    Page<User> findAllByNameSurnameLikeAndDeletedFalse(String nameSurname, Pageable pageable);

    Optional<User> findUserByUsernameAndDeletedFalse(String username);
    List<User> findAllByDeletedFalse();
}
