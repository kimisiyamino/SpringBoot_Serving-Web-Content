package com.eleonoralion.servingwebcontent.controller;

import com.eleonoralion.servingwebcontent.entity.Role;
import com.eleonoralion.servingwebcontent.entity.User;
import com.eleonoralion.servingwebcontent.service.MessageService;
import com.eleonoralion.servingwebcontent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/admin/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserAdminController {

    private final UserService userService;
    private final MessageService messageService;

    @Autowired
    public UserAdminController(UserService userService,
                               MessageService messageService) {

        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping
    public String getUsersPage(Map<String, Object> model){
        model.put("userList", userService.findAllByOrderById());
        return "admin/users";
    }

    @GetMapping("/edit/{user}")
    public String getEditUserPage(@PathVariable User user,
                           Model model,
                           HttpSession httpSession){

        // Добавляем аттрибуты User & Role
        addAttributes(user, model);

        // Сохраняем в нашу сессию id пользователя (и заголовок), которого мы редактируем
        httpSession.setAttribute("editId", user.getId());
        httpSession.setAttribute("headerUserName", user.getId() + ": " + user.getUsername());

        return "admin/editUser";
    }

    @PostMapping("/edit")
    public String saveUser(@RequestParam Map<String, String> form,
                           HttpSession httpSession,
                           Model model,
                           @Valid User user,
                           BindingResult bindingResult){

        // Добавляем к "форме" потерянный id из сессии
        user.setId((Long)httpSession.getAttribute("editId"));

        // Наполняем User ролями из Form
        userService.fillUserRolesFromMap(user, form);

        // Проверка ролей
        if(user.getRoles().size() == 0)
            return addAttributesAndErrorMessage(user, model, "Роли не выбраны!");

        // Проверка данных с формы
        if(bindingResult.hasErrors())
            return addAttributesAndErrorMessage(user, model, "");

        // Проверяем уникальность новых введённых login & email
        if(!userService.checkByUsernameAndNotThisId(user.getUsername(), user.getId()) )
            return addAttributesAndErrorMessage(user, model, "Пользователь с таким логином уже существует!");

        if(!userService.checkByEmailAndNotThisId(user.getEmail(), user.getId()))
            return addAttributesAndErrorMessage(user, model, "Пользователь с таким email уже существует!");


        // Сохраняем пользователя
        userService.updateUser(user);
        return "redirect:/admin/user?edit";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") User author){

        // Удаляем привязку user к message
        messageService.removeUserMessageBinding(author);

        // Удаляем пользователя
        userService.deleteById(author.getId());

        return "redirect:/admin/user?delete";
    }

    @GetMapping("/send/{user}")
    public String sendEmail(@PathVariable("user") User user){

        // Отключаем галочку подтвержденного email
        user.setConfirmEmail(false);

        // Отправляем код активации на почту User
        userService.sendActivationCode(user);

        // Обновляем данные User (activationCode)
        userService.updateUser(user);

        return "redirect:/admin/user/edit/{user}?send";
    }

    private String addAttributesAndErrorMessage(User user, Model model, String errorMessage) {
        model.addAttribute("errorMessage", errorMessage);
        addAttributes(user, model);
        return "admin/editUser";
    }

    private void addAttributes(User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
    }
}
