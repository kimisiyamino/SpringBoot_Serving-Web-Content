package com.eleonoralion.servingwebcontent.controller;

import com.eleonoralion.servingwebcontent.models.MessageModel;
import com.eleonoralion.servingwebcontent.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ServingWebContentController {

    private final MessageRepository messageRepository;

    private static final String MAIN_PATH = "/main";

    @Autowired
    public ServingWebContentController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/")
    public String hello(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }


    @GetMapping(MAIN_PATH)
    public String main(@RequestParam(name = "success", required = false) String success, Map<String, Object> model) {

        model.put("messagesList", (List<MessageModel>)messageRepository.findAll());
        model.put("messageModel", new MessageModel("", ""));
        model.put("filter", false);

        if (success != null)
            switch (success){
                case "add": model.put("info", "Успешно добавлено сообщение"); break;
                case "delete": model.put("info", "Успешно удалено сообщение"); break;
            }

        return MAIN_PATH;
    }

    @PostMapping(MAIN_PATH)
    public String addMessage(Map<String, Object> model, @Valid MessageModel messageModel, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            model.put("messagesList", messageRepository.findAll());
            model.put("filter", false);
            return MAIN_PATH;
        }

        messageRepository.save(messageModel);
        return "redirect:" + MAIN_PATH + "?success=add";
    }

    @PostMapping("/delete/{id}")
    public String deleteMessage(@PathVariable Long id) {
        Optional<MessageModel> optionalMessage = messageRepository.findById(id);

         /*
            if(optionalMessage.isPresent()){
                messageRepository.delete(optionalMessage.get());
         */
        optionalMessage.ifPresent(messageRepository::delete);
        return "redirect:" + MAIN_PATH + "?success=delete";
    }

    
    @PostMapping("/filter")
    public String filterMessage(@RequestParam String filter, Map<String, Object> model){
        List<MessageModel> messages = new ArrayList<>();

        if(!filter.isEmpty())
            messages = messageRepository.findByTagContaining(filter);

        model.put("messagesList", messages);
        model.put("messageModel", new MessageModel("", ""));
        model.put("filter", true);
        return MAIN_PATH;
    }
}