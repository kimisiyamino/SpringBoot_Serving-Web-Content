package com.eleonoralion.servingwebcontent.controller;

import com.eleonoralion.servingwebcontent.entity.MessageModel;
import com.eleonoralion.servingwebcontent.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/admin/message")
@PreAuthorize("hasAuthority('ADMIN')")
public class ServingWebContentMessageController {

    private final MessageRepository messageRepository;

    @Autowired
    public ServingWebContentMessageController(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    @GetMapping
    public String getMessages(Map<String, Object> model){
        model.put("messageList", messageRepository.findAllByOrderById());
        return "admin/messages";
    }

    @PostMapping("/delete/{id}")
    public String deleteMessage(@PathVariable Long id) {
        messageRepository.deleteById(id);
        return "redirect:/main?delete";
    }

    @GetMapping("/edit/{id}")
    public String getPage(@PathVariable("id") MessageModel messageModel, Model model){
        model.addAttribute("messageModel", messageModel);
        return "admin/editMessage";
    }

    @PostMapping("/edit")
    public String editMessage(@RequestParam("text") String text,
                              @RequestParam("tag") String tag,
                              @RequestParam("dateTime") String dateTime,
                              @RequestParam("id") MessageModel messageModel){

        messageModel.setText(text);
        messageModel.setTag(tag);
        messageModel.setDateTime(LocalDateTime.parse(dateTime));
        messageRepository.save(messageModel);

        return "redirect:/main";
    }
}
