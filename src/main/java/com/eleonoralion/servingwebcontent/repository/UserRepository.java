package com.eleonoralion.servingwebcontent.repository;

import com.eleonoralion.servingwebcontent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByOrderById();

    User findByUsername(String username);
    User findByEmail(String email);
    User findByActivationCode(String code);

    @Query("FROM User WHERE username LIKE :username AND NOT id = :id")
    User findByUsernameAndNotThisId(@Param("username") String username,
                                    @Param("id") Long id);
    @Query("FROM User WHERE email LIKE :email AND NOT id = :id")
    User findByEmailAndNotThisId(@Param("email") String email,
                                 @Param("id") Long id);
}
