package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping ("/account/")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping ("/register")
    public String registerAccount (Model model, Account account) {
        model.addAttribute("account", account);
        return "account-registration-form";
    }

    @PostMapping ("/register")
    public String registerAccount (@Valid Account account, BindingResult result, String passwordConfirm, ModelMap modelMap, Model model) {
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
}
