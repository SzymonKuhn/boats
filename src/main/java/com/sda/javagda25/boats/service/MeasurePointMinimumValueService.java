package com.sda.javagda25.boats.service;

import com.sda.javagda25.boats.model.MeasurePoint;
import com.sda.javagda25.boats.model.MeasurePointMinimumValue;
import com.sda.javagda25.boats.model.dto.MeasurePointDto;
import com.sda.javagda25.boats.repository.MeasurePointMinimumValueRepository;
import com.sda.javagda25.boats.repository.MeasurePointRepository;
import com.sda.javagda25.boats.repository.MeasurePointStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MeasurePointMinimumValueService {
    @Autowired
    private MeasurePointMinimumValueRepository measurePointMinimumValueRepository;

    public List<MeasurePointMinimumValue> getByBoat (Long boatId) {
        List<MeasurePointMinimumValue> listMinimumValues = new ArrayList<>();
        listMinimumValues.addAll(measurePointMinimumValueRepository.findAllByBoat_Id(boatId));
        return listMinimumValues;
    }

    public List<MeasurePointMinimumValue> getByBoatAndMeasurePoint (Long boatId, Long measurePointId) {
        List<MeasurePointMinimumValue> listMinimumValues = new ArrayList<>();
        listMinimumValues.addAll(measurePointMinimumValueRepository.findAllByBoatAndMeasurePoint(boatId, measurePointId));
        return listMinimumValues;
    }

    public void save (MeasurePointMinimumValue measurePointMinimumValue) {
        measurePointMinimumValueRepository.save(measurePointMinimumValue);
    }

    public void delete (Long minimumValueId) {
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
