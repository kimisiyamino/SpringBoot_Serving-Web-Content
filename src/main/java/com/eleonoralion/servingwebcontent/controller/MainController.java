package com.eleonoralion.servingwebcontent.controller;

import com.eleonoralion.servingwebcontent.entity.Message;
import com.eleonoralion.servingwebcontent.entity.User;
import com.eleonoralion.servingwebcontent.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
public class MainController {

    private final MessageService messageService;

    @Autowired
    public MainController(MessageService messageService) {
        this.messageService = messageService;
    }


    @GetMapping("/")
    public String getHelloPage() {

        // Стартовая страница сервиса
        return "hello";
    }

    @GetMapping("/login")
    public String getLoginPage(@AuthenticationPrincipal User user){

        // Если "пришедший" User не авторизован
        if(user == null)
            return "login";
        else
            return "redirect:/main";
    }


    @GetMapping("/main")
    public String getMainPage(@RequestParam(name = "filter", required = false) String filter,
                       Map<String, Object> model,
                       Message message) {

        if(filter == null || filter.isEmpty()) {
            model.put("messagesList", messageService.findAllByOrderById());
            model.put("filter", false);
        } else {
            model.put("messagesList", messageService.findByTagContainingIgnoreCase(filter));
            model.put("filter", true);
        }

        model.put("filterValue", filter);

        return "/main";
    }

    @PostMapping("/main")
    public String addMessage(@AuthenticationPrincipal User user,
                             Map<String, Object> model,
                             @Valid Message message,
                             BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            model.put("messagesList", messageService.findAllByOrderById());
            model.put("filter", false);
            return "/main";
        }

        messageService.addMessage(message, user);

        return "redirect:/main?add";
    }
}