package com.eleonoralion.servingwebcontent.controller;

import com.eleonoralion.servingwebcontent.entity.MessageModel;
import com.eleonoralion.servingwebcontent.entity.Role;
import com.eleonoralion.servingwebcontent.entity.User;
import com.eleonoralion.servingwebcontent.repository.MessageRepository;
import com.eleonoralion.servingwebcontent.repository.UserRepository;
import com.eleonoralion.servingwebcontent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserAdminController {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;

    @Autowired
    public UserAdminController(UserRepository userRepository,
                               MessageRepository messageRepository,
                               UserService userService) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.userService = userService;
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

        // Logs
        System.out.println("\n====================================================\nUSER FOR EDIT:");
        System.out.println(user.toString());
        System.out.println("====================================================");

        return "admin/editUser";
    }

    @PostMapping("/edit")
    public String saveUser(@RequestParam Map<String, String> form,
                           Model model,
                           @Valid User user, BindingResult bindingResult){
        // Logs
        System.out.println("MODEL USER:");
        System.out.println(user.toString());
        System.out.println("====================================================");

        System.out.println("FORM VALUES:");
        form.forEach((key1, value) -> System.out.println(key1 + " " + value));
        System.out.println("====================================================");

            // Наполняем User ролями из Form

        // Создаём Set из String наименований ролей из enum Role
        Set<String> roles = Arrays.stream(Role.values()).map(role -> role.name()).collect(Collectors.toSet());
        // Очищаем текущего User от roles
        user.getRoles().clear();
        // Ищем ключи из формы по именам ролей созданного нами Set, и добавляем нашему User, если таковые ключи равны
        for (String key : form.keySet()) {
            if(roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }

        System.out.println("MODEL USER AFTER EDITING:");
        System.out.println(user.toString());
        System.out.println("====================================================");

            // Обработка ошибок
        // Roles
        if(user.getRoles().size() == 0){
            model.addAttribute("errorMessage", "Роли не выбраны!");
            model.addAttribute("user", user);
            model.addAttribute("roles", Role.values());
            return "admin/editUser";
        }

        // username, password, email, registrationDate
        // firstName, lastName, age... - в процессе
        if(bindingResult.hasErrors()){
            model.addAttribute("user", user);
            model.addAttribute("roles", Role.values());

            for(ObjectError objectError : bindingResult.getAllErrors()){
                System.out.println(objectError.toString() + "\n" + objectError.getDefaultMessage());
            }

            return "admin/editUser";
        }

        // Проверяем новые введённые login & email
        if(!userService.findByUsernameAndNotThisId(user.getUsername(), user.getId()) ){
            model.addAttribute("errorMessage", "Пользователь с таким логином уже существует!");
            model.addAttribute("user", user);
            model.addAttribute("roles", Role.values());
            return "admin/editUser";
        }
        if(!userService.findByEmailAndNotThisId(user.getEmail(), user.getId())){
            model.addAttribute("errorMessage", "Пользователь с таким email уже существует!");
            model.addAttribute("user", user);
            model.addAttribute("roles", Role.values());
            return "admin/editUser";
        }

        // Сохраняем пользователя
        userService.updateUser(user);
        return "redirect:/admin/user?edit";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") User author){
        // Ищем все сообщения пользователя
        List<MessageModel> messages = messageRepository.findAllByAuthor(author);
        // Удаляем привязку автора
        for (MessageModel messageModel : messages){
            messageModel.setAuthor(null);
            messageRepository.save(messageModel);
        }
        // Удаляем пользователя
        userRepository.deleteById(author.getId());
        return "redirect:/admin/user?delete";
    }

    @GetMapping("/send/{user}")
    public String sendEmail(@PathVariable("user") User user){
        user.setActivationCode(UUID.randomUUID().toString());
        user.setConfirmEmail(false);
        userService.sendEmailMessage(user);
        userService.updateUser(user);
        return "redirect:/admin/user/edit/{user}?send";
    }
}
