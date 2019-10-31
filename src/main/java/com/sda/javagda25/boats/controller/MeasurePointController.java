package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.service.MeasurePointService;
import com.sda.javagda25.boats.service.MeasurePointStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/measurePoint/")
public class MeasurePointController {
    private MeasurePointService measurePointService;
    private MeasurePointStateService measurePointStateService;

    @Autowired
    public MeasurePointController(MeasurePointService measurePointService, MeasurePointStateService measurePointStateService) {
        this.measurePointService = measurePointService;
        this.measurePointStateService = measurePointStateService;
    }

    @GetMapping ("/list")
    public String listAll (Model model) {
        model.addAttribute("measurePoints", measurePointService.findAllMeasurePoints());
        return "measure-point-list";
    }

    @GetMapping ("/states/list")
    public String listAllStates (Model model) {
        model.addAttribute("measurePointStates", measurePointStateService.findAllMeasurePointsStates());
        return "measure-point-states-list";
    }


    @GetMapping ("/updateMeasurePoints")
    public String updateMeasurePoints () {
        measurePointService.updateMeasurePoints();
        return "redirect:/measurePoint/list";
    }
    @GetMapping ("/updateMeasurePointStates")
    public String updateMeasurePointStates () {
        measurePointStateService.updateMeasurePointsStates();
        return "redirect:/measurePoint/states/list";
    }

}
