package com.eleonoralion.servingwebcontent.controller;

import com.eleonoralion.servingwebcontent.entity.MessageModel;
import com.eleonoralion.servingwebcontent.entity.Role;
import com.eleonoralion.servingwebcontent.entity.User;
import com.eleonoralion.servingwebcontent.repository.MessageRepository;
import com.eleonoralion.servingwebcontent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    private final UserRepository userRepository;

    private final MessageRepository messageRepository;

    @Autowired
    public UserController(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @GetMapping
    public String getUsers(Map<String, Object> model){
        model.put("userList", userRepository.findAllByOrderById());
        return "admin/users";
    }

    @GetMapping("/edit/{user}")
    public String editUser(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "admin/editUser";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") User author){
        List<MessageModel> messages = messageRepository.findAllByAuthor(author);
        for (MessageModel messageModel : messages){
            messageModel.setAuthor(null);
            messageRepository.save(messageModel);
        }
        userRepository.deleteById(author.getId());
        return "redirect:/admin/user?delete";
    }

    @PostMapping("/edit")
    public String saveUser(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("registrationDate") String registrationDate,
                           @RequestParam Map<String, String> form,
                           @RequestParam("id") User user){

        Set<String> roles = Arrays.stream(Role.values()).map(role -> role.name()).collect(Collectors.toSet());
        user.getRoles().clear();

        for (String key : form.keySet()) {
            if(roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }

        user.setUsername(username);
        user.setPassword(password);
        user.setRegistrationDate(LocalDateTime.parse(registrationDate));
        user.setActive(form.containsKey("active"));

        userRepository.save(user);

        return "redirect:/admin/user?edit";
    }
}
