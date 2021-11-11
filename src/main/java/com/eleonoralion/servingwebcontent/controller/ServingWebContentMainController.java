package com.eleonoralion.servingwebcontent.controller;

import com.eleonoralion.servingwebcontent.entity.MessageModel;
import com.eleonoralion.servingwebcontent.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ServingWebContentMainController {

    private final MessageRepository messageRepository;


    private static final String MAIN_PATH = "/main";

    @Autowired
    public ServingWebContentMainController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    @GetMapping("/")
    public String hello(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }


    @GetMapping("/login")
    public String login(){
        if (isAuthenticated()) {
            return "redirect:/main";
        }
        return "login";
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }


    @GetMapping(MAIN_PATH)
    public String main(Map<String, Object> model, MessageModel messageModel) {

        model.put("messagesList", (List<MessageModel>)messageRepository.findAll());
       // model.put("messageModel", new MessageModel("", ""));
        model.put("filter", false);

        return MAIN_PATH;
    }


    @PostMapping(MAIN_PATH)
    public String addMessage(Map<String, Object> model, @Valid MessageModel messageModel, BindingResult bindingResult){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(bindingResult.hasErrors()){
            model.put("messagesList", messageRepository.findAll());
            model.put("filter", false);
            return MAIN_PATH;
        }

        messageModel.setAuthor(authentication.getName());
        messageRepository.save(messageModel);
        return "redirect:" + MAIN_PATH + "?add";
    }

    @PostMapping("/delete/{id}")
    public String deleteMessage(@PathVariable Long id) {
        Optional<MessageModel> optionalMessage = messageRepository.findById(id);

         /*
            if(optionalMessage.isPresent()){
                messageRepository.delete(optionalMessage.get());
         */
        optionalMessage.ifPresent(messageRepository::delete);
        return "redirect:" + MAIN_PATH + "?delete";
    }


    @PostMapping("/filter")
    public String filterMessage(@RequestParam String filter, Map<String, Object> model, MessageModel messageModel){
        List<MessageModel> messages = new ArrayList<>();

        if(!filter.isEmpty())
            messages = messageRepository.findByTagContaining(filter);

        model.put("messagesList", messages);
       // model.put("messageModel", new MessageModel("", ""));
        model.put("filter", true);
        return MAIN_PATH;
    }
}