package com.eleonoralion.servingwebcontent.repository;

import com.eleonoralion.servingwebcontent.entity.MessageModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface MessageRepository extends CrudRepository<MessageModel, Long> {
    List<MessageModel> findByTagContainingIgnoreCase(String tag);
    List<MessageModel> findByTagContaining(String tag);
    List<MessageModel> findAllByOrderById();
}
