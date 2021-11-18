package com.eleonoralion.servingwebcontent.repository;

import com.eleonoralion.servingwebcontent.entity.Picture;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PictureRepository extends CrudRepository<Picture, Long> {
    List<Picture> findAllByOrderById();
}
