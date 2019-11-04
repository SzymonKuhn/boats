package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.model.Boat;
import com.sda.javagda25.boats.model.MeasurePointMinimumValue;
import com.sda.javagda25.boats.model.MeasurePointState;
import com.sda.javagda25.boats.model.dto.ActualAndMinimumStatesForBoatDto;
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

import java.util.ArrayList;
import java.util.List;

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
        return "minimumValue-edit";
    }

    @GetMapping ("/actualStates/{boatId}")
    public String actualStatesForMinValues (Model model, @PathVariable(name = "boatId") Long boatId) {
        Boat boat = boatService.getById(boatId);
        List<MeasurePointMinimumValue> minValuesOfMeasurePointsForBoat = measurePointMinimumValueService.getByBoat(boatId);
        List<ActualAndMinimumStatesForBoatDto> actualAndMinimumStates = new ArrayList<>();
        for (MeasurePointMinimumValue measurePointMinimumValue : minValuesOfMeasurePointsForBoat) {
            ActualAndMinimumStatesForBoatDto actualAndMinimumStatesForBoatDto = new ActualAndMinimumStatesForBoatDto();
            actualAndMinimumStatesForBoatDto.setBoat(boat);
            actualAndMinimumStatesForBoatDto.setMeasurePoint(measurePointMinimumValue.getMeasurePoint());
            actualAndMinimumStatesForBoatDto.setMinimumValue(measurePointMinimumValue.getMinimumValue());
            actualAndMinimumStatesForBoatDto.setWarningValue(measurePointMinimumValue.getWarningValue());
            MeasurePointState actualMeasurePointState = measurePointStateService.getActualMeasurePointStateByPointId(measurePointMinimumValue.getMeasurePoint().getId());
            actualAndMinimumStatesForBoatDto.setMeasureDateTime(actualMeasurePointState.getMeasureDateTime());
            actualAndMinimumStatesForBoatDto.setWaterState(actualMeasurePointState.getWaterState());
            actualAndMinimumStates.add(actualAndMinimumStatesForBoatDto);
        }
        model.addAttribute("actualAndMinValues", actualAndMinimumStates);
        model.addAttribute("boat", boat);
        return "actual-states-for-boat";
    }

}
