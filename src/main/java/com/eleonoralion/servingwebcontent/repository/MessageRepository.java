package com.eleonoralion.servingwebcontent.repository;

import com.eleonoralion.servingwebcontent.entity.MessageModel;
import com.eleonoralion.servingwebcontent.entity.User;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface MessageRepository extends CrudRepository<MessageModel, Long> {
    List<MessageModel> findByTagContainingIgnoreCase(String tag);
    List<MessageModel> findByTagContaining(String tag);
    List<MessageModel> findAllByOrderById();
    List<MessageModel> findAllByAuthorOrderById(User author);
    List<MessageModel> findAllByAuthor(User author);
    void deleteAllByAuthor(User author);
}
