package com.eleonoralion.servingwebcontent.controller;

import com.eleonoralion.servingwebcontent.entity.Message;
import com.eleonoralion.servingwebcontent.entity.User;
import com.eleonoralion.servingwebcontent.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/admin/message")
@PreAuthorize("hasAuthority('ADMIN')")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @GetMapping()
    public String getMessagesPage(Map<String, Object> model){
        model.put("messagesList", messageService.findAllByOrderById());
        return "admin/messages";
    }

    @GetMapping("/{userId}")
    public String getMessagesPageById(@PathVariable(name = "userId", required = true) User user,
                              Map<String, Object> model){
        model.put("messagesList", messageService.findAllByAuthorOrderById(user));
        return "admin/messages";
    }


    @PostMapping("/delete/{id}")
    public String deleteMessage(@PathVariable Long id,
                                HttpServletRequest request) {
        messageService.deleteById(id);
        return "redirect:" + request.getHeader("referer").split("\\?")[0] + "?delete";
    }


    @GetMapping("/edit/{id}")
    public String getEditMessagePage(@PathVariable("id") Message message,
                          Model model,
                          HttpServletRequest request,
                          HttpSession session){

        model.addAttribute("messageModel", message);
        session.setAttribute("returnURL", request.getHeader("referer").split("\\?")[0]);
        return "admin/editMessage";
    }

    @PostMapping("/edit")
    public String editMessage(@RequestParam("text") String text,
                              @RequestParam("tag") String tag,
                              @RequestParam("dateTime") String dateTime,
                              @RequestParam("id") Message message,
                              HttpSession session){

        message.setText(text);
        message.setTag(tag);
        message.setDateTime(LocalDateTime.parse(dateTime));
        messageService.save(message);

        return "redirect:" + session.getAttribute("returnURL") + "?edit";
    }
}
