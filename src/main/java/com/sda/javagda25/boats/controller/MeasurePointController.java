package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.model.*;
import com.sda.javagda25.boats.model.dto.BoatWithMinimumValueAndActualStateDto;
import com.sda.javagda25.boats.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/measurePoint/")
public class MeasurePointController {
    private MeasurePointService measurePointService;
    private MeasurePointStateService measurePointStateService;
    private AccountService accountService;
    private MeasurePointMinimumValueService measurePointMinimumValueService;
    private BoatService boatService;

    @Autowired
    public MeasurePointController(MeasurePointService measurePointService,
                                  MeasurePointStateService measurePointStateService,
                                  AccountService accountService,
                                  MeasurePointMinimumValueService measurePointMinimumValueService,
                                  BoatService boatService) {
        this.measurePointService = measurePointService;
        this.measurePointStateService = measurePointStateService;
        this.accountService = accountService;
        this.measurePointMinimumValueService = measurePointMinimumValueService;
        this.boatService = boatService;
    }

    @GetMapping("/list")
    public String listAll(Model model,
                          @RequestParam(name = "sort", defaultValue = "pointName") String sort,
                          @RequestParam(name = "search", defaultValue = "") String search,
                          @RequestParam(name = "page", defaultValue = "0") int page,
                          @RequestParam(name = "size", defaultValue = "20") int size) {
        Page<MeasurePoint> measurePointsPage = measurePointService.measurePointPage(search, search, PageRequest.of(page, size, Sort.by(sort)));
        model.addAttribute("sort", sort);
        model.addAttribute("search", search);
        model.addAttribute("measurePoints", measurePointsPage);
        return "measure-point-list";
    }

    @GetMapping("/updateMeasurePoints")
    public String updateMeasurePoints() {
        measurePointService.updateMeasurePoints();
        return "redirect:/measurePoint/list";
    }

    @GetMapping("/updateMeasurePointStates")
    public String updateMeasurePointStates() {
        measurePointStateService.updateMeasurePointsStates();
        return "redirect:/measurePoint/states/list";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Model model, @PathVariable(name = "id") Long id, Principal principal) {
        MeasurePoint measurePoint = measurePointService.getById(id);
        List<MeasurePointState> states = measurePointStateService.findMeasurePointStatesByPointId(measurePoint).stream()
                .limit(10)
                .collect(Collectors.toList());
        List<BoatWithMinimumValueAndActualStateDto> boatsWithMinValues = new ArrayList<>();
        if (principal != null) {
            Account account = accountService.getByUsername(principal.getName());
            List<Boat> boats = boatService.findAllByUsername(account.getUsername());
            for (Boat boat : boats) {
                Optional<MeasurePointMinimumValue> optional = measurePointMinimumValueService.getByBoatAndMeasurePoint(boat, measurePoint);
                if (optional.isPresent()) {
                    MeasurePointMinimumValue minimumValue = optional.get();
                    BoatWithMinimumValueAndActualStateDto boatWithMinimumValue = new BoatWithMinimumValueAndActualStateDto();
                    boatWithMinimumValue.setBoat(boat);
                    boatWithMinimumValue.setMeasurePointId(measurePoint.getId());
                    boatWithMinimumValue.setMinimumValue(minimumValue.getMinimumValue());
                    boatWithMinimumValue.setWarningValue(minimumValue.getWarningValue());
                    boatsWithMinValues.add(boatWithMinimumValue);
                }
            }
        }
        model.addAttribute("states", states);
        model.addAttribute("actualState", states.get(0).getWaterState());
        model.addAttribute("point", measurePoint);
        model.addAttribute("boats", boatsWithMinValues);
        return "measure-point-details";
    }
}
