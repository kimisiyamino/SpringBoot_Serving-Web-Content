package com.eleonoralion.servingwebcontent.controller;

import com.eleonoralion.servingwebcontent.form.EditProfileForm;
import com.eleonoralion.servingwebcontent.entity.User;
import com.eleonoralion.servingwebcontent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public String getProfilePage(@PathVariable("username") String username, Model model){
        model.addAttribute("user", userService.findByUsername(username));
        return "profile";
    }

    @GetMapping("/edit/{id}")
    public String getEditProfilePage(@PathVariable("id") User user, Model model){
        model.addAttribute("editProfileForm", new EditProfileForm(user));
        return "editProfile";
    }

    @PostMapping("/edit")
    public String editProfile(Model model,
                              @AuthenticationPrincipal User user,
                              @Valid EditProfileForm editProfileForm,
                              BindingResult bindingResult){

        // Если данные с формы некорректны
        if(bindingResult.hasErrors()){
            // Добавляем к "форме" потерянный headerName
            editProfileForm.setHeaderName(user.getId() + ": " + user.getUsername());
            return "editProfile";
        }

        // Проверяем уникальность новых введённых login & email
        if(!userService.checkByUsernameAndNotThisId(editProfileForm.getUsername(), user.getId()))
            return addAttributesAndErrorMessage(model, editProfileForm, "Пользователь с таким логином уже существует!");

        if(!userService.checkByEmailAndNotThisId(editProfileForm.getEmail(), user.getId()))
            return addAttributesAndErrorMessage(model, editProfileForm, "Пользователь с таким email уже существует!");

        // Если почта была изменена
        if(!user.getEmail().equals(editProfileForm.getEmail())){
            user.setEmail(editProfileForm.getEmail());
            user.setConfirmEmail(false);
            userService.sendActivationCode(user);
        }

        // Заполняем данные из формы
        user.setUsername(editProfileForm.getUsername());
        user.setPassword(editProfileForm.getPassword());
        user.getUserInfo().setFirstName(editProfileForm.getFirstName());
        user.getUserInfo().setLastName(editProfileForm.getLastName());
        user.getUserInfo().setAge(editProfileForm.getAge());

        // Обновляем данные в БД
        userService.updateUser(user);

        // Редирект
        return getProfilePage(user.getUsername(), model);
    }

    private String addAttributesAndErrorMessage(Model model, EditProfileForm editProfileForm, String errorMessage){
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("editProfileForm", editProfileForm);
        return "admin/editUser";
    }
}
