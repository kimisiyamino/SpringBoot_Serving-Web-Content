package com.eleonoralion.servingwebcontent.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ServingWebContentErrorController implements ErrorController {

    @RequestMapping("/error")
    public String getErrorPage() {
        return "error";
    }
}
