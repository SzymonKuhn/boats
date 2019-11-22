package com.sda.javagda25.boats.service;

import com.sda.javagda25.boats.model.MeasurePointState;
import com.sda.javagda25.boats.model.dto.MeasurePointWithTendencyDto;
import com.sda.javagda25.boats.model.jsonDto.AddressToLngLat;
import com.sda.javagda25.boats.model.jsonDto.Location;
import com.sda.javagda25.boats.model.MeasurePoint;
import com.sda.javagda25.boats.model.dto.MeasurePointDto;
import com.sda.javagda25.boats.repository.MeasurePointRepository;
import com.sda.javagda25.boats.repository.MeasurePointStateRepository;
import javafx.concurrent.ScheduledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

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
//                measurePoint = addGeolocation(measurePoint);
                measurePointRepository.save(measurePoint);
//            } else if (measurePointRepository.findById(measurePointDto.getId_stacji()).get().getLat() == null ||
//                    measurePointRepository.findById(measurePointDto.getId_stacji()).get().getLng() == null) {
//                MeasurePoint measurePoint = measurePointRepository.findById(measurePointDto.getId_stacji()).get();
//                measurePoint = addGeolocation(measurePoint);
//                measurePointRepository.save(measurePoint);
            }
        }

//        adding geolocations for measure points based on csv file
        try {
            File file = ResourceUtils.getFile("classpath:hydro-locations/hydro-locations.csv");
            Scanner  scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String[] arrayGeolocation = scanner.nextLine().split(",");
                Long id = Long.parseLong(arrayGeolocation[0]);
                if (measurePointRepository.existsById(id)) {
                    MeasurePoint measurePoint = measurePointRepository.getOne(id);
                    String lat = arrayGeolocation[6].substring(0,2) + "." + arrayGeolocation[6].substring(2);
                    String lng = arrayGeolocation[5].substring(0,2) + "." + arrayGeolocation[5].substring(2);
                    measurePoint.setLat(lat);
                    measurePoint.setLng(lng);
                    measurePointRepository.save(measurePoint);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

    public List<MeasurePointWithTendencyDto> getMeasurePointsWithTendency() {
        List<MeasurePointWithTendencyDto> measurePointWithTendencyList = new ArrayList<>();
        List<MeasurePoint> measurePointsWithCoordinates = measurePointRepository.findAll().stream()
                .filter(p -> p.getLng() != null && p.getLat() != null)
                .collect(Collectors.toList());

        for (MeasurePoint measurePoint : measurePointsWithCoordinates) {
            MeasurePointWithTendencyDto measurePointWithTendencyDto = new MeasurePointWithTendencyDto();
            measurePointWithTendencyDto.setId(measurePoint.getId());
            measurePointWithTendencyDto.setLat(measurePoint.getLat());
            measurePointWithTendencyDto.setLng(measurePoint.getLng());
            measurePointWithTendencyDto.setPointName(measurePoint.getPointName());
            measurePointWithTendencyDto.setRiverName(measurePoint.getRiverName());

            List<MeasurePointState> states = measurePointStateRepository.findAllByMeasurePointOrderByMeasureDateTimeDesc(measurePoint);
            if (states.size() > 1){
                if (states.get(0).getWaterState() > states.get(1).getWaterState()) {
                    measurePointWithTendencyDto.setUpTendency(true);
                    measurePointWithTendencyDto.setDownTendency(false);
                } else if (states.get(0).getWaterState() < states.get(1).getWaterState()) {
                    measurePointWithTendencyDto.setUpTendency(false);
                    measurePointWithTendencyDto.setDownTendency(true);
                } else {
                    measurePointWithTendencyDto.setUpTendency(false);
                    measurePointWithTendencyDto.setDownTendency(false);
                }
                measurePointWithTendencyList.add(measurePointWithTendencyDto);
            }
        }
        return measurePointWithTendencyList;
    }


    private List<MeasurePoint> tryFindMeasurePointByStringId(String input) { //todo how to find object by part of id?
        try {
            Long id = Long.parseLong(input);
            return measurePointRepository.findAllByIdIsLike(id);
        } catch (NumberFormatException nfe) {
            nfe.getMessage();
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
