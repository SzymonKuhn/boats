package com.sda.javagda25.boats.repository;

import com.sda.javagda25.boats.model.Boat;
import com.sda.javagda25.boats.model.MeasurePoint;
import com.sda.javagda25.boats.model.MeasurePointMinimumValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeasurePointMinimumValueRepository extends JpaRepository<MeasurePointMinimumValue, Long> {
    List<MeasurePointMinimumValue> findAllByBoat_Id(Long boatId);

    Optional<MeasurePointMinimumValue> findByBoatAndAndMeasurePoint(Boat boat, MeasurePoint measurePoint);
}
