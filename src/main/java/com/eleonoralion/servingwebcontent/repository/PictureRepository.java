package com.eleonoralion.servingwebcontent.repository;

import com.eleonoralion.servingwebcontent.entity.UserPhoto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PictureRepository extends CrudRepository<UserPhoto, Long> {
    List<UserPhoto> findAllByOrderById();
}
