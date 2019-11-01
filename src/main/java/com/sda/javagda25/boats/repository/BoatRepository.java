package com.sda.javagda25.boats.repository;

import com.sda.javagda25.boats.model.Boat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoatRepository extends JpaRepository <Boat, Long> {
    List<Boat> findAllByAccount_Id (Long accountId);
}
