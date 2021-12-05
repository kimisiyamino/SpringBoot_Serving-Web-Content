package com.eleonoralion.servingwebcontent.repository;

import com.eleonoralion.servingwebcontent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByOrderById();

    User findByUsername(String username);
    User findByEmail(String email);
    User findByActivationCode(String code);
}
