package com.eleonoralion.servingwebcontent.controller;

import com.eleonoralion.servingwebcontent.entity.MessageModel;
import com.eleonoralion.servingwebcontent.entity.User;
import com.eleonoralion.servingwebcontent.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class ServingWebContentMainController {

    private final MessageRepository messageRepository;

    private static final String MAIN_PATH = "/main";

    @Autowired
    public ServingWebContentMainController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    @GetMapping("/")
    public String hello() {
        return "hello";
    }

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal User user){
        if(user == null)
            return "login";
        else
            return "redirect:/main";
    }


    @GetMapping(MAIN_PATH)
    public String main(Map<String, Object> model, MessageModel messageModel) {
        model.put("messagesList", messageRepository.findAllByOrderById());
        model.put("filter", false);

        return MAIN_PATH;
    }

    @PostMapping(MAIN_PATH)
    public String addMessage(@AuthenticationPrincipal User user, Map<String, Object> model, @Valid MessageModel messageModel, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            model.put("messagesList", messageRepository.findAll());
            model.put("filter", false);
            return MAIN_PATH;
        }

        messageModel.setAuthor(user);
        messageModel.setDateTime(LocalDateTime.now());
        messageRepository.save(messageModel);
        return "redirect:" + MAIN_PATH + "?add";
    }

    @PostMapping("/delete/{id}")
    public String deleteMessage(@PathVariable Long id) {
        messageRepository.findById(id).ifPresent(messageRepository::delete);
        return "redirect:" + MAIN_PATH + "?delete";
    }


    @PostMapping("/filter")
    public String filterMessage(@RequestParam String filter, Map<String, Object> model, MessageModel messageModel){

        List<MessageModel> messages = new ArrayList<>();

        if(!filter.isEmpty())
            messages = messageRepository.findByTagContainingIgnoreCase(filter);

        model.put("messagesList", messages);
        model.put("filter", true);
        return MAIN_PATH;
    }
}