package com.eleonoralion.servingwebcontent.controller;

import com.eleonoralion.servingwebcontent.form.RegistrationForm;
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
import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) { ;
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String getRegistrationPage(@AuthenticationPrincipal User user,
                                      RegistrationForm registrationForm){
        return user == null ? "registration" : "redirect:/main";
    }

    @PostMapping("/registration")
    public String registration(Map<String, Object> model,
                               @Valid RegistrationForm registrationForm,
                               BindingResult bindingResult){

        // Если поля с формы некорректны
        if(bindingResult.hasErrors()){
            return "registration";
        }

        // Если пользователь есть
        if(!userService.checkByUsername(registrationForm.getUsername())){
            model.put("message", "Пользователь с таким логином уже существует!");
            return "registration";
        }
        if(!userService.checkByEmail(registrationForm.getEmail())){
            model.put("message", "Пользователь с таким email уже существует!");
            return "registration";
        }

        // Формируем User из формы
        User user = new User(registrationForm);

        // Отправляем письмо и добавляем User в БД
        userService.sendActivationCode(user);
        userService.addUser(user);

        // Если всё успешно
        return "success";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model,
                           @PathVariable String code){
        boolean isActivated = userService.activateCode(code);

        if(isActivated){
            model.addAttribute("message", "Пользователь активирован");
        }else{
            model.addAttribute("message", "Пользователь не найден");
        }

        return "login";
    }
}