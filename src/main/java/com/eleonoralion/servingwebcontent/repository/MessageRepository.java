package com.eleonoralion.servingwebcontent.repository;

import com.eleonoralion.servingwebcontent.models.MessageModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<MessageModel, Long> {
    List<MessageModel> findByTagContainingIgnoreCase(String tag);
    List<MessageModel> findByTagContaining(String tag);
}
