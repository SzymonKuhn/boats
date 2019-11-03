package com.sda.javagda25.boats.repository;

import com.sda.javagda25.boats.model.Boat;
import com.sda.javagda25.boats.model.MeasurePointMinimumValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasurePointMinimumValueRepository extends JpaRepository <MeasurePointMinimumValue, Long> {
    List<MeasurePointMinimumValue> findAllByBoatAndMeasurePoint (Long boatId, Long measurePointId);
    List<MeasurePointMinimumValue> findAllByBoat_Id (Long boatId);

}
