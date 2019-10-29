package com.sda.javagda25.boats.repository;


import com.sda.javagda25.boats.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository <Account, Long> {
    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

}
