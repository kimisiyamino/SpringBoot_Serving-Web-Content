package com.eleonoralion.servingwebcontent.repository;

import com.eleonoralion.servingwebcontent.entity.Message;
import com.eleonoralion.servingwebcontent.entity.User;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findByTagContainingIgnoreCase(String tag);
    List<Message> findByTagContaining(String tag);
    List<Message> findAllByOrderById();
    List<Message> findAllByAuthorOrderById(User author);
    List<Message> findAllByAuthor(User author);
    void deleteAllByAuthor(User author);
}
