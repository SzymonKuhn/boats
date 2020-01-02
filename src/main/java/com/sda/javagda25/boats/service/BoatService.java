package com.sda.javagda25.boats.service;

import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.Boat;
import com.sda.javagda25.boats.repository.AccountRepository;
import com.sda.javagda25.boats.repository.BoatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class BoatService {
    private BoatRepository boatRepository;
    private AccountRepository accountRepository;

    @Autowired
    public BoatService(BoatRepository boatRepository, AccountRepository accountRepository) {
        this.boatRepository = boatRepository;
        this.accountRepository = accountRepository;
    }


    public List<Boat> findAllByUsername(String username) {
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);
        if (optionalAccount.isPresent()) {
            Long accountId = optionalAccount.get().getId();
            return boatRepository.findAllByAccount_Id(accountId);
        } else {
            throw new EntityNotFoundException();
        }
    }

    public Long save(Boat boat) {
        Boat boatSaved = boatRepository.save(boat);
        return boatSaved.getId();
    }

    public void delete(Long boatId) {
        Optional<Boat> optionalBoat = boatRepository.findById(boatId);
        if (optionalBoat.isPresent()) {
            boatRepository.delete(optionalBoat.get());
        } else {
            throw new EntityNotFoundException();
        }
    }

    public Boat getById(Long boatId) {
        Optional<Boat> optionalBoat = boatRepository.findById(boatId);
        if (optionalBoat.isPresent()) {
            return optionalBoat.get();
        } else {
            throw new EntityNotFoundException();
        }
    }
}
