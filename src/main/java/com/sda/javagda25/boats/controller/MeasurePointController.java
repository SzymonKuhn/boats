package com.sda.javagda25.boats.controller;

import com.google.gson.Gson;
import com.sda.javagda25.boats.model.MeasurePoint;
import com.sda.javagda25.boats.model.MeasurePointState;
import com.sda.javagda25.boats.service.MeasurePointService;
import com.sda.javagda25.boats.service.MeasurePointStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping ("/states/latest")
    public String listLatestStates (Model model) {
        model.addAttribute("measurePointStates", measurePointStateService.findActualMeasurePointsStates());
        return "measure-point-states-list";
    }


    @GetMapping ("/details/{id}")
    public String getDetails (Model model, @PathVariable (name = "id") Long id) {
        MeasurePoint measurePoint = measurePointService.getById(id);
        List<MeasurePointState> states = measurePointStateService.findMeasurePointStatesByPointId(measurePoint).stream()
                .limit(10)
                .collect(Collectors.toList());
        model.addAttribute("states", states);
        model.addAttribute("point", measurePoint);
        return "measure-point-details";
    }

    @PostMapping ("/searchMeasurePoints")
    public String search (String input, Model model) {
        List<MeasurePoint> measurePoints = measurePointService.search(input);
        if (measurePoints.isEmpty()) {
            model.addAttribute("errorMessage", "No records found.");
            model.addAttribute("measurePoints", measurePointService.findAllMeasurePoints());
            return "measure-point-list";
        }
        model.addAttribute("measurePoints", measurePoints);
        return "measure-point-list";
    }



}
