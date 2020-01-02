package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.Boat;
import com.sda.javagda25.boats.model.MeasurePointMinimumValue;
import com.sda.javagda25.boats.model.dto.BoatPhotoAddDto;
import com.sda.javagda25.boats.service.AccountService;
import com.sda.javagda25.boats.service.BoatService;
import com.sda.javagda25.boats.service.MeasurePointMinimumValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/boat/")
public class BoatController {
    private BoatService boatService;
    private AccountService accountService;
    private MeasurePointMinimumValueService measurePointMinimumValueService;

    @Autowired
    public BoatController(BoatService boatService, AccountService accountService, MeasurePointMinimumValueService measurePointMinimumValueService) {
        this.boatService = boatService;
        this.accountService = accountService;
        this.measurePointMinimumValueService = measurePointMinimumValueService;
    }

    @GetMapping("/add")
    public String add(Model model, Boat boat) {
        model.addAttribute("boat", boat);
        return "boat-add";
    }

    @PostMapping("/add")
    public String add(Boat boat, Principal principal) {
        Account account = accountService.getByUsername(principal.getName());
        if (boat == null) {
            return "redirect:/account/details";
        }
        if (boat.getAccount() == null) {
            boat.setAccount(account);
        }

        Long boatId = boatService.save(boat);

        if (account.getDefaultBoat() == null) {
            account.setDefaultBoat(boatService.getById(boatId));
            accountService.save(account);
        }
        return "redirect:/boat/list";
    }


    @GetMapping("/addPhoto/{boatId}")
    public String addPhoto(Model model, BoatPhotoAddDto boatPhotoAddDto) {
        model.addAttribute("boatPhotoAddDto", boatPhotoAddDto);
        return "boat-photo-form";
    }

    @PostMapping("/addPhoto")
    public String addPhoto(Model model, Principal principal, BoatPhotoAddDto boatPhotoAddDto) {
        Account account = accountService.getByUsername(principal.getName());
        Boat boat = boatService.getById(boatPhotoAddDto.getBoatId());
        if (!boat.getAccount().equals(account)) {
            return "redirect:/account/details";
        }
        MultipartFile photo = boatPhotoAddDto.getPhoto();
        if (photo != null) {
            try {
                boat.setPhoto(photo.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            boatService.save(boat);
        }
        return "redirect:/account/details";
    }

    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable(name = "id") Long boatId) {
        Boat boat = boatService.getById(boatId);
        List<MeasurePointMinimumValue> minValues = measurePointMinimumValueService.getByBoat(boatId);
        model.addAttribute("boat", boat);
        model.addAttribute("minValues", minValues);
        return "boat-details";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable(name = "id") Long boatId) {
        Boat boat = boatService.getById(boatId);
        model.addAttribute("boat", boat);
        return "boat-add";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Long boatId, Principal principal) {
        Boat boat = boatService.getById(boatId);
        Account account = accountService.getByUsername(principal.getName());
        if (boat.getAccount().getUsername().equals(account.getUsername())) {
            boatService.delete(boatId);
            if (account.getDefaultBoat().getId() == boatId) {
                account.setDefaultBoat(null);
                accountService.save(account);
            }
        }
        return "redirect:/account/details";
    }

    @GetMapping("/list")
    public String listAccountBoats(Model model, Principal principal) {
        List<Boat> boats = boatService.findAllByUsername(principal.getName());
        Account user = accountService.getByUsername(principal.getName());
        if (user.getDefaultBoat() != null) {
            model.addAttribute("defaultBoatId", user.getDefaultBoat().getId());
        } else {
            model.addAttribute("defaultBoatId", null);
        }
        model.addAttribute("boats", boats);
        return "boat-list";
    }

    @GetMapping("/setDefault/{id}")
    public String setDefault(Principal principal, @PathVariable(name = "id") Long boatId) {
        Account account = accountService.getByUsername(principal.getName());
        account.setDefaultBoat(boatService.getById(boatId));
        accountService.save(account);
        return "redirect:/boat/list";
    }
}
