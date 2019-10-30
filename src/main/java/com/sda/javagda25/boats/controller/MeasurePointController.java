package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.service.MeasurePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/measurePoint/")
public class MeasurePointController {
    private MeasurePointService measurePointService;

    @Autowired
    public MeasurePointController(MeasurePointService measurePointService) {
        this.measurePointService = measurePointService;
    }

    @GetMapping ("/list")
    public String listAll (Model model) {
        model.addAttribute("measurePoints", measurePointService.findAllMeasurePoints());
        model.addAttribute("measurePointsStates", measurePointService.findAllMeasurePointsStates());



        return "measure-point-list";
    }


    @GetMapping ("/update")
    public String updateDatabase () {
        measurePointService.transformJsonFromApiToMeasurePointsAndStates();
        return "redirect:/measurePoint/list";
    }

}
