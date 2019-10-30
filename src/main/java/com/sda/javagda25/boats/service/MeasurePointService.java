package com.sda.javagda25.boats.service;

import com.sda.javagda25.boats.model.MeasurePoint;
import com.sda.javagda25.boats.model.MeasurePointDto;
import com.sda.javagda25.boats.model.MeasurePointState;
import com.sda.javagda25.boats.repository.MeasurePointRepository;
import com.sda.javagda25.boats.repository.MeasurePointStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class MeasurePointService {
    private String UrlAddress = "https://danepubliczne.imgw.pl/api/data/hydro";

    @Autowired
    private MeasurePointRepository measurePointRepository;
    @Autowired
    private MeasurePointStateRepository measurePointStateRepository;
    @Autowired
    private RestTemplate restTemplate;

    public void transformJsonFromApiToMeasurePointsAndStates() {
        ResponseEntity<List<MeasurePointDto>> measurePointResponseEntity = restTemplate.exchange(UrlAddress,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<MeasurePointDto>>() {
                });

        List<MeasurePointDto> measurePointDtos = measurePointResponseEntity.getBody();
        for (MeasurePointDto measurePointDto : measurePointDtos) {
            if (!measurePointRepository.existsById(measurePointDto.getId_stacji())) {
                measurePointRepository.save(new MeasurePoint(measurePointDto));
            }
            MeasurePointState measurePointState = new MeasurePointState(measurePointDto);
            if (measurePointDto.getStan_wody_data_pomiaru() != null) {
                if (!measurePointStateRepository.existsByIdStationAndAndMeasureDateTime(measurePointState.getIdStation(), measurePointState.getMeasureDateTime())) {

                    measurePointState.setMeasurePoint(getById(measurePointState.getIdStation()));

                    measurePointStateRepository.save(measurePointState);
                }
            }

        }
    }

    public List<MeasurePoint> findAllMeasurePoints() {
        return measurePointRepository.findAll();
    }

    public List<MeasurePointState> findAllMeasurePointsStates() {
        return measurePointStateRepository.findAll();
    }


    public MeasurePoint getById(Long id) {
        Optional<MeasurePoint> optional = measurePointRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new EntityNotFoundException();
        }
    }
}
