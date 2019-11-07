package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.model.MeasurePoint;
import com.sda.javagda25.boats.repository.MeasurePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class IndexController {
    @Autowired
    MeasurePointRepository measurePointRepository;

    @GetMapping ("/index")
    public String getIndex (Principal principal, Model model) {
        List<MeasurePoint> measurePointsWithCoordinates = measurePointRepository.findAll().stream()
                .filter(p -> p.getLng() != null && p.getLat() != null)
                .collect(Collectors.toList());
        model.addAttribute("points", measurePointsWithCoordinates);
        if (principal!=null) {
            model.addAttribute("username", principal.getName());
        }
        return "index";
    }



    @GetMapping ("/login")
    public String login () {
        return "account-login";
    }
}
