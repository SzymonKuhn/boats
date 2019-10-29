package com.sda.javagda25.boats.service;

import com.sda.javagda25.boats.repository.AccountRoleRepository;
import com.sda.javagda25.boats.model.AccountRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountRoleService {
    private AccountRoleRepository accountRoleRepository;

    @Autowired
    public AccountRoleService(AccountRoleRepository accountRoleRepository) {
        this.accountRoleRepository = accountRoleRepository;
    }

    public Set<AccountRole> getBasicUserRoles() {
        Optional<AccountRole> user = accountRoleRepository.findByName("USER");
        Set<AccountRole> roles = new HashSet<>();
        if (user.isPresent()) {
            roles.add(user.get());
        }
        return roles;
    }

}
