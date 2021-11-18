package com.eleonoralion.servingwebcontent.controller;

import com.eleonoralion.servingwebcontent.entity.MessageModel;
import com.eleonoralion.servingwebcontent.entity.User;
import com.eleonoralion.servingwebcontent.repository.MessageRepository;
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

    private final MessageRepository messageRepository;

    @Autowired
    public MessageController(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    @GetMapping()
    public String getMessages(Map<String, Object> model){
        model.put("messagesList", messageRepository.findAllByOrderById());
        return "admin/messages";
    }

    @GetMapping("/{userId}")
    public String getMessages(@PathVariable(name = "userId", required = true) User user, Map<String, Object> model){
        model.put("messagesList", messageRepository.findAllByAuthorOrderById(user));
        return "admin/messages";
    }


    @PostMapping("/delete/{id}")
    public String deleteMessage(@PathVariable Long id, HttpServletRequest request) {
        messageRepository.deleteById(id);
        return "redirect:" + request.getHeader("referer").split("\\?")[0] + "?delete";
    }


    @GetMapping("/edit/{id}")
    public String getPage(@PathVariable("id") MessageModel messageModel,
                          Model model,
                          HttpServletRequest request, HttpSession session){

        model.addAttribute("messageModel", messageModel);
        session.setAttribute("returnURL", request.getHeader("referer").split("\\?")[0]);
        return "admin/editMessage";
    }

    @PostMapping("/edit")
    public String editMessage(@RequestParam("text") String text,
                              @RequestParam("tag") String tag,
                              @RequestParam("dateTime") String dateTime,
                              @RequestParam("id") MessageModel messageModel,
                              HttpSession session){

        messageModel.setText(text);
        messageModel.setTag(tag);
        messageModel.setDateTime(LocalDateTime.parse(dateTime));
        messageRepository.save(messageModel);

        return "redirect:" + session.getAttribute("returnURL") + "?edit";
    }
}
