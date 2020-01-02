package com.sda.javagda25.boats.service;

import com.sda.javagda25.boats.repository.AccountRepository;
import com.sda.javagda25.boats.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    private AccountRoleService accountRoleService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountRoleService accountRoleService, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountRoleService = accountRoleService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean register(Account account) {
        if (accountRepository.existsByUsername(account.getUsername())) {
            return false;
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAccountRoles(accountRoleService.getBasicUserRoles());
        accountRepository.save(account);
        return true;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account getById (Long id) {
        Optional<Account> optional = accountRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new EntityNotFoundException("account, id: " + id);
        }
    }

    public Account getByUsername(String name) {
        Optional<Account> optionalAccount = accountRepository.findByUsername(name);
        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    public void save (Account account)
    {
        accountRepository.save(account);
    }

}
