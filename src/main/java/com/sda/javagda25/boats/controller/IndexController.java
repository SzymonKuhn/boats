package com.sda.javagda25.boats.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class IndexController {

    @GetMapping ("/index")
    public String getIndex (Principal principal, Model model) {
        if (principal!=null) {
            model.addAttribute("username", principal.getName());
        }
        return "index";
    }

    @GetMapping ("/indexLogged")
    public String getIndexLogged () {
        return "indexLogged";
    }

    @GetMapping ("/login")
    public String login () {
        return "account-login";
    }
}
