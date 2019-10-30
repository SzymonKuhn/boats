package com.sda.javagda25.boats.repository;


import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.MeasurePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeasurePointRepository extends JpaRepository <MeasurePoint, Long> {
    boolean existsById(Long id);

}