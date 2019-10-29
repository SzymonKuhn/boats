package com.sda.javagda25.boats.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping ("/index")
    public String getIndex () {
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
