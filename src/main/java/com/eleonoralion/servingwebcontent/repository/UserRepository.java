package com.eleonoralion.servingwebcontent.repository;

import com.eleonoralion.servingwebcontent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
