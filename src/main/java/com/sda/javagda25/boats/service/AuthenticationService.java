package com.sda.javagda25.boats.service;

import com.sda.javagda25.boats.repository.AccountRepository;
import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.AccountRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    private AccountRepository accountRepository;

    @Autowired
    public AuthenticationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public UserDetails loadUserByUsername (String username) {
        Optional<Account> optional = accountRepository.findByUsername(username);
        if (optional.isPresent()) {
            Account account = optional.get();

            String[] roles = account.getAccountRoles()
                    .stream()
                    .map(AccountRole::getName)
                    .toArray(String[]::new);

            return User.builder()
                    .username(account.getUsername())
                    .password(account.getPassword())
                    .accountLocked(account.isLocked())
                    .roles(roles)
                    .build();
        }
        throw new UsernameNotFoundException("username not found: " + username);
    }
}
