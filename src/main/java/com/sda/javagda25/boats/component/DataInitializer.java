package com.sda.javagda25.boats.component;

import com.sda.javagda25.boats.repository.AccountRepository;
import com.sda.javagda25.boats.repository.AccountRoleRepository;
import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.AccountRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private AccountRepository accountRepository;
    private AccountRoleRepository accountRoleRepository;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public DataInitializer(AccountRepository accountRepository, AccountRoleRepository accountRoleRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountRoleRepository = accountRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        addAccountRole("ADMIN");
        addAccountRole("USER");
        addAccount("user", "user", "USER");
        addAccount("admin", "admin", "USER", "ADMIN");
    }

    private void addAccountRole(String role) {
        if (!accountRoleRepository.existsByName(role)) {
            AccountRole accountRole = new AccountRole();
            accountRole.setName(role);
            accountRoleRepository.save(accountRole);
        }
    }

    private void addAccount(String username, String password, String... roles) {
        if (!accountRepository.existsByUsername(username)) {
            Account account = new Account();
            account.setUsername(username);
            account.setPassword(passwordEncoder.encode(password));
            account.setAccountRoles(findRoles(roles));
            accountRepository.save(account);
        }
    }

    private Set<AccountRole> findRoles(String[] roles) {
        Set<AccountRole> roleSet = new HashSet<>();
        for (String role : roles) {
            if (accountRoleRepository.existsByName(role)) {
                roleSet.add(accountRoleRepository.findByName(role).get());
            }
        }
        return roleSet;
    }
}
