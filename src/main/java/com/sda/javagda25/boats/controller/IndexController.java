package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.model.MeasurePoint;
import com.sda.javagda25.boats.model.dto.MeasurePointWithTendencyDto;
import com.sda.javagda25.boats.repository.MeasurePointRepository;
import com.sda.javagda25.boats.service.MeasurePointService;
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
    private MeasurePointRepository measurePointRepository;
    @Autowired
    private MeasurePointService measurePointService;

    @GetMapping ("/index")
    public String getIndex (Principal principal, Model model) {


        List<MeasurePointWithTendencyDto> measurePointsWithTendency = measurePointService.getMeasurePointsWithTendency();


        model.addAttribute("points", measurePointsWithTendency);
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
