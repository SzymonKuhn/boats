package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.model.*;
import com.sda.javagda25.boats.model.dto.ActualAndMinimumStatesForBoatDto;
import com.sda.javagda25.boats.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/account/")
public class AccountController {

    private AccountService accountService;
    private AccountRoleService accountRoleService;
    private BoatService boatService;
    private MeasurePointMinimumValueService measurePointMinimumValueService;
    private MeasurePointStateService measurePointStateService;
    private MeasurePointService measurePointService;

    @Autowired
    public AccountController(AccountService accountService, AccountRoleService accountRoleService, BoatService boatService, MeasurePointMinimumValueService measurePointMinimumValueService, MeasurePointStateService measurePointStateService, MeasurePointService measurePointService) {
        this.accountService = accountService;
        this.accountRoleService = accountRoleService;
        this.boatService = boatService;
        this.measurePointMinimumValueService = measurePointMinimumValueService;
        this.measurePointStateService = measurePointStateService;
        this.measurePointService = measurePointService;
    }


    @GetMapping("/register")
    public String registerAccount(Model model, Account account) {
        model.addAttribute("account", account);
        return "account-registration-form";
    }

    @PostMapping("/register")
    public String registerAccount(@Valid Account account, BindingResult result, String passwordConfirm, ModelMap modelMap, Model model) {
        account.setAccountRoles(new HashSet<>(accountRoleService.getBasicUserRoles())); //todo adding default roles
        if (result.hasErrors()) {
            return registrationError(account, model, result.getFieldError().getDefaultMessage());
        }

        if (!account.getPassword().equals(passwordConfirm)) {
            return registrationError(account, model, "Password doesn't match");
        }

        if (!accountService.register(account)) {
            return registrationError(account, model, "User already exists");
        }
        return "redirect:/login";
    }

    private String registrationError(Account account, Model model, String defaultMessage) {
        model.addAttribute("account", account);
        model.addAttribute("message", defaultMessage);
        return "account-registration-form";
    }


    @GetMapping("/list")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public String getAll(Model model) {
        model.addAttribute("accounts", accountService.findAll());
        model.addAttribute("allRoles", accountRoleService.findAll());
        return "account-list";
    }

    @GetMapping("/roles/{id}")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public String accountRoles(Model model,
                               @PathVariable(name = "id") Long id) {
        Account account = accountService.getById(id);
        model.addAttribute("account", account);
        return "account-roles";
    }

    @GetMapping("/details")
    public String details(Model model, Principal principal) {
        Account account = accountService.getByUsername(principal.getName());
        List<ActualAndMinimumStatesForBoatDto> actualAndMinimumStates = null;

        if (account.getDefaultBoat() != null) {
            Boat defaultBoat = account.getDefaultBoat();
            List<MeasurePointMinimumValue> minValuesOfMeasurePointsForBoat = measurePointMinimumValueService.getByBoat(defaultBoat.getId());
            actualAndMinimumStates = new ArrayList<>();
            for (MeasurePointMinimumValue measurePointMinimumValue : minValuesOfMeasurePointsForBoat) {
                MeasurePointState actualMeasurePointState = measurePointStateService.getActualMeasurePointStateByPointId(measurePointMinimumValue.getMeasurePoint().getId());
                MeasurePoint measurePoint = measurePointService.getById(actualMeasurePointState.getIdStation());
                ActualAndMinimumStatesForBoatDto actualAndMinimumStatesForBoatDto = new ActualAndMinimumStatesForBoatDto.Builder()
                        .withBoatId(measurePointMinimumValue.getBoat().getId())
                        .withMeasurePoint(measurePointMinimumValue.getMeasurePoint())
                        .withMinimumValue(measurePointMinimumValue.getMinimumValue())
                        .withWarningValue(measurePointMinimumValue.getWarningValue())
                        .withMeasureDateTime(actualMeasurePointState.getMeasureDateTime())
                        .withWaterState(actualMeasurePointState.getWaterState())
                        .withLat(measurePoint.getLat())
                        .withLng(measurePoint.getLng())
                        .build();
                actualAndMinimumStates.add(actualAndMinimumStatesForBoatDto);
            }
        }
        model.addAttribute("actualAndMinValues", actualAndMinimumStates);
        model.addAttribute("account", account);

        return "account-details";
    }

    @GetMapping("/details/edit")
    public String detailsEdit(Model model, Principal principal) {
        model.addAttribute("account", accountService.getByUsername(principal.getName()));
        return "account-details-form";
    }

    @PostMapping("/details/edit")
    public String detailsEdit(Account account, Principal principal) {
        if (!account.getUsername().equals(principal.getName())) {
            return "redirect:/index";
        }
        accountService.save(account);
        return "redirect:/account/details";
    }


    @GetMapping("/addPhoto")
    public String addPhoto() {
        return "account-photo-form";
    }

    @PostMapping("/addPhoto")
    public String addPhoto(Model model, Principal principal, @RequestParam("photo") MultipartFile photo) {
        Account account = accountService.getByUsername(principal.getName());
        if (photo != null) {
            try {
                account.setPhoto(photo.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            accountService.save(account);
        }
        return "redirect:/account/details";
    }
}
