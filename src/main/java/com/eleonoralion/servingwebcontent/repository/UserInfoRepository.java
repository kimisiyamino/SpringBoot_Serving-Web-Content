package com.eleonoralion.servingwebcontent.repository;

import com.eleonoralion.servingwebcontent.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
