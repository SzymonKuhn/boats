package com.sda.javagda25.boats.service;

import com.sda.javagda25.boats.model.MeasurePoint;
import com.sda.javagda25.boats.model.dto.MeasurePointDto;
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
import java.util.Set;

@Service
public class MeasurePointStateService {
    private String UrlAddress = "https://danepubliczne.imgw.pl/api/data/hydro";

    @Autowired
    private MeasurePointRepository measurePointRepository;

    @Autowired
    private MeasurePointStateRepository measurePointStateRepository;
    @Autowired
    private RestTemplate restTemplate;

    public void updateMeasurePointsStates() {
        ResponseEntity<List<MeasurePointDto>> measurePointResponseEntity = restTemplate.exchange(UrlAddress,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<MeasurePointDto>>() { });

        List<MeasurePointDto> measurePointDtos = measurePointResponseEntity.getBody();
        for (MeasurePointDto measurePointDto : measurePointDtos) {
            MeasurePointState measurePointState = new MeasurePointState(measurePointDto);
            if (measurePointDto.getStan_wody_data_pomiaru() != null) {
                if (!measurePointStateRepository.existsByIdStationAndMeasureDateTime(measurePointState.getIdStation(), measurePointState.getMeasureDateTime())) {

                    Optional<MeasurePoint> optionalMeasurePoint = measurePointRepository.findById(measurePointState.getIdStation());
                    if (optionalMeasurePoint.isPresent()) {
                        measurePointState.setMeasurePoint(optionalMeasurePoint.get());
                        measurePointStateRepository.save(measurePointState);
                    } else {
                        throw new EntityNotFoundException();
                    }
                }
            }
        }
    }



    public List<MeasurePointState> findAllMeasurePointsStates() {
        return measurePointStateRepository.findAll();
    }

    public List<MeasurePointState> findActualMeasurePointsStates() {
        return measurePointStateRepository.findLatestStates();
    }


    public MeasurePointState getActualMeasurePointStateByPointId (Long measurePointId) {
        Optional<MeasurePointState> optionalMeasurePointState = measurePointStateRepository.findLatestStateOfMeasurePoint(measurePointId);
        if (optionalMeasurePointState.isPresent()) {
            return optionalMeasurePointState.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

}
