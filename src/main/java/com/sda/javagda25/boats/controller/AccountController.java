package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.service.AccountRoleService;
import com.sda.javagda25.boats.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.HashSet;

@Controller
@RequestMapping ("/account/")
public class AccountController {

    private AccountService accountService;
    private AccountRoleService accountRoleService;

    @Autowired
    public AccountController(AccountService accountService, AccountRoleService accountRoleService) {
        this.accountService = accountService;
        this.accountRoleService = accountRoleService;
    }


    @GetMapping ("/register")
    public String registerAccount (Model model, Account account) {
        model.addAttribute("account", account);
        return "account-registration-form";
    }

    @PostMapping ("/register")
    public String registerAccount (@Valid Account account, BindingResult result, String passwordConfirm, ModelMap modelMap, Model model) {
        account.setAccountRoles(new HashSet<>(accountRoleService.getBasicUserRoles()));
        if (result.hasErrors()) {
            return registrationError (account, model, result.getFieldError().getDefaultMessage());
        }

        if(!account.getPassword().equals(passwordConfirm)) {
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


    @GetMapping ("/list")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public String getAll (Model model) {
        model.addAttribute("accounts", accountService.findAll());
        model.addAttribute("allRoles", accountRoleService.findAll());
        return "account-list";
    }

    @GetMapping ("/roles/{id}")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public String accountRoles (Model model,
                                @PathVariable (name = "id") Long id) {
        Account account = accountService.getById(id);
        model.addAttribute("account", account);
        return "account-roles";
    }

    @GetMapping ("/details")
    public String details (Model model, Principal principal) {
        Account account = accountService.getByUsername(principal.getName());
        if (account.getPhoto() != null) {
            model.addAttribute("photo", Base64.getEncoder().encodeToString(account.getPhoto()));
        }
        model.addAttribute("account", account);

        return "account-details";
    }

    @GetMapping ("/details/edit")
    public String detailsEdit (Model model, Principal principal) {
        model.addAttribute("account", accountService.getByUsername(principal.getName()));
        return "account-details-form";
    }

    @PostMapping ("/details/edit")
    public String detailsEdit (Account account, Principal principal) {
        if (!account.getUsername().equals(principal.getName())) {
            return "redirect:/index";
        }
        accountService.save(account);
        return "redirect:/account/details";
    }


    @GetMapping ("/addPhoto")
    public String addPhoto () {
        return "account-photo-form";
    }

    @PostMapping ("/addPhoto")
    public String addPhoto (Model model, Principal principal, @RequestParam ("photo") MultipartFile photo) {
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
