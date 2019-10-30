package com.sda.javagda25.boats.repository;


import com.sda.javagda25.boats.model.MeasurePoint;
import com.sda.javagda25.boats.model.MeasurePointState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MeasurePointStateRepository extends JpaRepository <MeasurePointState, Long> {
    boolean existsByIdStationAndAndMeasureDateTime (Long idStation, LocalDateTime localDateTime);

}
