package com.sda.javagda25.boats.service;

import com.sda.javagda25.boats.model.AddressToLngLat;
import com.sda.javagda25.boats.model.Location;
import com.sda.javagda25.boats.model.MeasurePoint;
import com.sda.javagda25.boats.model.dto.MeasurePointDto;
import com.sda.javagda25.boats.repository.MeasurePointRepository;
import com.sda.javagda25.boats.repository.MeasurePointStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class MeasurePointService {
    private String urlMeasurePoints = "https://danepubliczne.imgw.pl/api/data/hydro";
    private String urlGeolocation = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    private String apiKey = "&key=AIzaSyDIlhefXdQwH7Eq9YFBHoIXDInMITZ264A";

    @Autowired
    private MeasurePointRepository measurePointRepository;
    @Autowired
    private MeasurePointStateRepository measurePointStateRepository;
    @Autowired
    private RestTemplate restTemplate;

    public void updateMeasurePoints() {
        ResponseEntity<List<MeasurePointDto>> measurePointResponseEntity = restTemplate.exchange(urlMeasurePoints,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<MeasurePointDto>>() {
                });

        List<MeasurePointDto> measurePointDtos = measurePointResponseEntity.getBody();
        for (MeasurePointDto measurePointDto : measurePointDtos) {
            if (!measurePointRepository.existsById(measurePointDto.getId_stacji())) {
                MeasurePoint measurePoint = new MeasurePoint(measurePointDto);
                measurePoint = addGeolocation(measurePoint);
                measurePointRepository.save(measurePoint);
            } else if (measurePointRepository.findById(measurePointDto.getId_stacji()).get().getLat() == null ||
                    measurePointRepository.findById(measurePointDto.getId_stacji()).get().getLat() == null) {
                MeasurePoint measurePoint = measurePointRepository.findById(measurePointDto.getId_stacji()).get();
                measurePoint = addGeolocation(measurePoint);
                measurePointRepository.save(measurePoint);
            }
        }
    }

    public List<MeasurePoint> findAllMeasurePoints() {
        return measurePointRepository.findAll();
    }


    public MeasurePoint getById(Long id) {
        Optional<MeasurePoint> optional = measurePointRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    public List<MeasurePoint> search(String input) {
        List<MeasurePoint> measurePoints = new ArrayList<>();
        measurePoints.addAll(tryFindMeasurePointByStringId(input));
        input = "%" + input + "%";
        measurePoints.addAll(measurePointRepository.findAllByPointNameIsLikeOrRiverNameIsLike(input, input));
        return measurePoints;
    }

    private List<MeasurePoint> tryFindMeasurePointByStringId(String input) { //todo how to find object by part of id?
        try {
            Long id = Long.parseLong(input);
            return measurePointRepository.findAllByIdIsLike(id);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return Collections.emptyList();
    }

    private MeasurePoint addGeolocation(MeasurePoint measurePoint) {
        String url = urlGeolocation + measurePoint.getPointName() + apiKey;
        ResponseEntity<AddressToLngLat> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, AddressToLngLat.class);
        AddressToLngLat body = responseEntity.getBody();
        if (!body.getStatus().equals("ZERO_RESULTS")) {
            Location location = body.getResults()[0].getGeometry().getLocation();
            measurePoint.setLat(location.getLat());
            measurePoint.setLng(location.getLng());
        }
        return measurePoint;
    }

}
