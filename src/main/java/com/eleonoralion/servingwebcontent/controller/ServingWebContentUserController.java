package com.eleonoralion.servingwebcontent.controller;

import com.eleonoralion.servingwebcontent.entity.User;
import com.eleonoralion.servingwebcontent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class ServingWebContentUserController {

    private final UserRepository userRepository;

    @Autowired
    public ServingWebContentUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getUsers(Map<String, Object> model){
        model.put("userList", userRepository.findAll());
        return "users";
    }

    @GetMapping("/edit/{user}")
    public String editUser(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id){
        userRepository.deleteById(id);
        return "redirect:/users?del";
    }

    @PostMapping("/edit")
    public String saveUser(){
        // ....
        //..
        //..
        //..
        //.....
        return "redirect:/users?ed";
    }
}
