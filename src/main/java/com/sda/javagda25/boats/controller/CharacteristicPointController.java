package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.Boat;
import com.sda.javagda25.boats.model.CharacteristicPoint;
import com.sda.javagda25.boats.service.AccountService;
import com.sda.javagda25.boats.service.CharacteristicPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/point/")
public class CharacteristicPointController {

    private CharacteristicPointService characteristicPointService;
    private AccountService accountService;

    @Autowired
    public CharacteristicPointController(CharacteristicPointService characteristicPointService, AccountService accountService) {
        this.characteristicPointService = characteristicPointService;
        this.accountService = accountService;
    }

    @PostMapping("/add")
    public String add (CharacteristicPoint characteristicPoint, Principal principal) {
        Account account = accountService.getByUsername(principal.getName());
        if (characteristicPoint == null) {
            return "redirect:/account/details";
        }
        if (characteristicPoint.getAccount() == null ){
            characteristicPoint.setAccount(account);
        }

        characteristicPointService.save(characteristicPoint);

        return "redirect:/point/list";
    }

    @GetMapping("/add")
    public String add (Model model, Boat boat) {
        model.addAttribute("boat", boat);
        return "point-add";
    }



    @GetMapping("/list")
    public String listAll (Model model) {
        model.addAttribute("characteristicPoints", characteristicPointService.findAllCharacteristicPoints());
        return "characteristic-point-list";
    }

}
