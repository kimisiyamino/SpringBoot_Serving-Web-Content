package com.eleonoralion.servingwebcontent.service;

import com.eleonoralion.servingwebcontent.entity.Message;
import com.eleonoralion.servingwebcontent.entity.User;
import com.eleonoralion.servingwebcontent.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public void removeUserMessageBinding(User user) {
        // Ищем все сообщения пользователя
        List<Message> messages = messageRepository.findAllByAuthor(user);
        // Удаляем привязку автора
        for (Message messageModel : messages){
            messageModel.setAuthor(null);
            messageRepository.save(messageModel);
        }
    }

    public List<Message> findAllByOrderById() {
        return messageRepository.findAllByOrderById();
    }

    public void save(Message message) {
        messageRepository.save(message);
    }

    public List<Message> findByTagContainingIgnoreCase(String filter) {
        return messageRepository.findByTagContainingIgnoreCase(filter);
    }

    public Iterable<Message> findAll() {
        return messageRepository.findAll();
    }

    public List<Message> findAllByAuthorOrderById(User user) {
        return messageRepository.findAllByAuthorOrderById(user);
    }

    public void deleteById(Long id) {
        messageRepository.deleteById(id);
    }

    public void addMessage(Message message, User user) {
        message.setAuthor(user);
        message.setDateTime(LocalDateTime.now().withNano(0));
        save(message);
    }
}
