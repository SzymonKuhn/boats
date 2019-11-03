package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.model.MeasurePointMinimumValue;
import com.sda.javagda25.boats.service.BoatService;
import com.sda.javagda25.boats.service.MeasurePointMinimumValueService;
import com.sda.javagda25.boats.service.MeasurePointService;
import com.sda.javagda25.boats.service.MeasurePointStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/minimumValue/")
public class MeasurePointMinimumValueController {
    private MeasurePointService measurePointService;
    private MeasurePointStateService measurePointStateService;
    private MeasurePointMinimumValueService measurePointMinimumValueService;
    private BoatService boatService;

    @Autowired
    public MeasurePointMinimumValueController(MeasurePointService measurePointService, MeasurePointStateService measurePointStateService, MeasurePointMinimumValueService measurePointMinimumValueService, BoatService boatService) {
        this.measurePointService = measurePointService;
        this.measurePointStateService = measurePointStateService;
        this.measurePointMinimumValueService = measurePointMinimumValueService;
        this.boatService = boatService;
    }

    @GetMapping ("/add/{id}")
    public String add (Model model, @PathVariable (name = "id") Long boatId, MeasurePointMinimumValue measurePointMinimumValue) {
        measurePointMinimumValue.setBoat(boatService.getById(boatId));
        model.addAttribute("minValue", measurePointMinimumValue);
        model.addAttribute("measurePonts", measurePointService.findAllMeasurePoints());
        return "minimumValue-add";
    }

    @PostMapping ("/add")
    public String add (MeasurePointMinimumValue measurePointMinimumValue) {
        measurePointMinimumValueService.save(measurePointMinimumValue);
        return "redirect:/boat/details/" + measurePointMinimumValue.getBoat().getId();
    }

    @GetMapping ("/edit/{id}")
    public String edit (@PathVariable (name = "id") Long id, Model model) {
        model.addAttribute("minValue", measurePointMinimumValueService.getById(id));
        model.addAttribute("measurePonts", measurePointService.findAllMeasurePoints());
        //todo add measure points
        return "minimumValue-edit";
    }


}
