package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.model.Boat;
import com.sda.javagda25.boats.model.MeasurePoint;
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
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/add/{id}")
    public String add(Model model, @PathVariable(name = "id") Long boatId, MeasurePointMinimumValue minValue) {
        minValue.setBoat(boatService.getById(boatId));
        model.addAttribute("minValue", minValue);
        model.addAttribute("measurePoints", measurePointService.findAllMeasurePoints());
        return "minimumValue-add";
    }

    @PostMapping("/add")
    public String add(MeasurePointMinimumValue measurePointMinimumValue, Model model) {
        if (measurePointMinimumValue.getMinimumValue() > measurePointMinimumValue.getWarningValue()) {
            model.addAttribute("errorMessage", "Warning value shouldn't be less then minimum value");
            model.addAttribute("minValue", measurePointMinimumValue);
            return "minimumValue-add";
        }
        measurePointMinimumValueService.save(measurePointMinimumValue);
        return "redirect:/boat/details/" + measurePointMinimumValue.getBoat().getId();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("minValue", measurePointMinimumValueService.getById(id));
        return "minimumValue-edit";
    }

    @GetMapping("/actualStates/{boatId}")
    public String actualStatesForMinValues(Model model, @PathVariable(name = "boatId") Long boatId) {
        Boat boat = boatService.getById(boatId);
        List<MeasurePointMinimumValue> minValuesOfMeasurePointsForBoat = measurePointMinimumValueService.getByBoat(boatId);
        List<ActualAndMinimumStatesForBoatDto> actualAndMinimumStates = new ArrayList<>();
        for (MeasurePointMinimumValue measurePointMinimumValue : minValuesOfMeasurePointsForBoat) {
            MeasurePointState actualMeasurePointState = measurePointStateService.getActualMeasurePointStateByPointId(measurePointMinimumValue.getMeasurePoint().getId());
            ActualAndMinimumStatesForBoatDto actualAndMinimumStatesForBoatDto = new ActualAndMinimumStatesForBoatDto.Builder()
                    .withBoat(measurePointMinimumValue.getBoat())
                    .withMeasurePoint(measurePointMinimumValue.getMeasurePoint())
                    .withMinimumValue(measurePointMinimumValue.getMinimumValue())
                    .withWarningValue(measurePointMinimumValue.getWarningValue())
                    .withMeasureDateTime(actualMeasurePointState.getMeasureDateTime())
                    .withWaterState(actualMeasurePointState.getWaterState())
                    .build();
            actualAndMinimumStates.add(actualAndMinimumStatesForBoatDto);
        }
        model.addAttribute("actualAndMinValues", actualAndMinimumStates);
        model.addAttribute("boat", boat);
        return "actual-states-for-boat";
    }


    @PostMapping("/searchMeasurePoints")
    public String search(String input, Long boatId, Model model) {
        MeasurePointMinimumValue minValue = new MeasurePointMinimumValue();
        minValue.setBoat(boatService.getById(boatId));
        List<MeasurePoint> measurePoints = new ArrayList<>();
        String[] inputs = input.split(" ");
        for (String s : inputs) {
            measurePoints.addAll(measurePointService.search(s));
        }

        int records = measurePoints.size();
        if (records == 0) {
            model.addAttribute("errorMessage", "No records found.");
            model.addAttribute("input", input.substring(1, input.length() - 1));
            measurePoints.addAll(measurePointService.findAllMeasurePoints());
        }

        model.addAttribute("minValue", minValue);
        model.addAttribute("measurePoints", measurePoints);
        return "minimumValue-add";
    }
}