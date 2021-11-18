package com.eleonoralion.servingwebcontent.controller;

import com.eleonoralion.servingwebcontent.entity.Role;
import com.eleonoralion.servingwebcontent.entity.User;
import com.eleonoralion.servingwebcontent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;
    @Autowired
    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/registration")
    public String getRegistrationPage(@AuthenticationPrincipal User authUser, User user, Map<String, Object> model){
        return authUser == null ? "registration" : "redirect:/main";
    }

    @PostMapping("/registration")
    public String registration(Map<String, Object> model, @Valid User user, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "registration";
        }

        if(userRepository.findByUsername(user.getUsername()) != null){
            model.put("message", "Пользователь уже существует!");
            return "registration";
        }

        user.setActive(true);
        user.setRegistrationDate(LocalDateTime.now().withNano(0));
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "success";
    }
}
