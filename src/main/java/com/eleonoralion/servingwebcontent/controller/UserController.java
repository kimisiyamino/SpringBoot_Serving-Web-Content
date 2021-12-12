package com.eleonoralion.servingwebcontent.controller;

import com.eleonoralion.servingwebcontent.entity.EditProfileForm;
import com.eleonoralion.servingwebcontent.entity.User;
import com.eleonoralion.servingwebcontent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/user/{username}")
    public String getProfilePage(@PathVariable("username") String username, Model model){
        model.addAttribute("user", userRepository.findByUsername(username));
        return "profile";
    }

    @GetMapping("/user/edit/{id}")
    public String getEditProfilePage(@PathVariable("id") User user, Model model){

        EditProfileForm editProfileForm = new EditProfileForm();

        editProfileForm.setId(user.getId());
        editProfileForm.setUsername(user.getUsername());
        editProfileForm.setPassword(user.getPassword());
        editProfileForm.setEmail(user.getEmail());
        editProfileForm.setConfirmEmail(user.getConfirmEmail());
        editProfileForm.setFirstName(user.getUserInfo().getFirstName());
        editProfileForm.setLastName(user.getUserInfo().getLastName());
        editProfileForm.setAge(user.getUserInfo().getAge());

        model.addAttribute("user", editProfileForm);

        return "editProfile";
    }

    @PostMapping("/user/edit")
    public String editProfile(@Valid EditProfileForm editProfileForm, BindingResult bindingResult){

        // Logs
        System.out.println("editProfileForm:");
        System.out.println(editProfileForm.toString());
        System.out.println("====================================================");


        return "editProfile";
    }
}
