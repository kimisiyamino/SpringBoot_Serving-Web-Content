package com.eleonoralion.servingwebcontent.controller;

import com.eleonoralion.servingwebcontent.entity.User;
import com.eleonoralion.servingwebcontent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/user/{username}")
    public String getProfilePage(@PathVariable("username") String username, Model model){
        model.addAttribute("user", userRepository.findByUsername(username));
        return "profile";
    }
}
