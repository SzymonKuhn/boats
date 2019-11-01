package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.Boat;
import com.sda.javagda25.boats.model.dto.BoatPhotoAddDto;
import com.sda.javagda25.boats.service.AccountService;
import com.sda.javagda25.boats.service.BoatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping ("/boat/")
public class BoatController {
    private BoatService boatService;
    private AccountService accountService;

    @Autowired
    public BoatController(BoatService boatService, AccountService accountService) {
        this.boatService = boatService;
        this.accountService = accountService;
    }

    @GetMapping("/add")
    public String add (Model model, Boat boat) {
        model.addAttribute("boat", boat);
        return "boat-add";
    }

    @PostMapping ("/add")
    public String add (Boat boat, Principal principal) {
        if (boat == null) {
            return "redirect:/account/details";
        }
        boat.setAccount(accountService.getByUsername(principal.getName()));
        boatService.save(boat);
        return "redirect:/account/details";
    }


    @GetMapping ("/addPhoto/{boatId}")
    public String addPhoto (Model model, BoatPhotoAddDto boatPhotoAddDto) {
        model.addAttribute("boatPhotoAddDto", boatPhotoAddDto);
        return "boat-photo-form";
    }

    @PostMapping ("/addPhoto")
    public String addPhoto (Model model, Principal principal, BoatPhotoAddDto boatPhotoAddDto) {
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
}
