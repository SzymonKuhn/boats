package com.sda.javagda25.boats.service;

import com.sda.javagda25.boats.model.Boat;
import com.sda.javagda25.boats.model.MeasurePoint;
import com.sda.javagda25.boats.model.MeasurePointMinimumValue;
import com.sda.javagda25.boats.repository.MeasurePointMinimumValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MeasurePointMinimumValueService {
    @Autowired
    private MeasurePointMinimumValueRepository measurePointMinimumValueRepository;

    public List<MeasurePointMinimumValue> getByBoat(Long boatId) {
        List<MeasurePointMinimumValue> listMinimumValues = new ArrayList<>();
        listMinimumValues.addAll(measurePointMinimumValueRepository.findAllByBoat_Id(boatId));
        return listMinimumValues;
    }

    public Optional<MeasurePointMinimumValue> getByBoatAndMeasurePoint(Boat boat, MeasurePoint measurePoint) {
        Optional<MeasurePointMinimumValue> optional = measurePointMinimumValueRepository.findByBoatAndAndMeasurePoint(boat, measurePoint);
        return optional;
    }

    public void save(MeasurePointMinimumValue measurePointMinimumValue) {
        measurePointMinimumValueRepository.save(measurePointMinimumValue);
    }

    public void delete(Long minimumValueId) {
        Optional<MeasurePointMinimumValue> optional = measurePointMinimumValueRepository.findById(minimumValueId);
        if (optional.isPresent()) {
            measurePointMinimumValueRepository.delete(optional.get());
        } else {
            throw new EntityNotFoundException();
        }
    }

    public MeasurePointMinimumValue getById(Long id) {
        Optional<MeasurePointMinimumValue> optional = measurePointMinimumValueRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new EntityNotFoundException();
        }

    }
}
