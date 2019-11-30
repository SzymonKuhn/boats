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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
//    private String urlGeolocation = "https://maps.googleapis.com/maps/api/geocode/json?address=";
//    private String apiKey = "&key=AIzaSyDIlhefXdQwH7Eq9YFBHoIXDInMITZ264A";

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
                measurePointRepository.save(measurePoint);
            }
        }

//        adding geolocations for measure points based on csv file
        try {
            File file = ResourceUtils.getFile("classpath:hydro-locations/hydro-locations.csv");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String[] arrayGeolocation = scanner.nextLine().split(",");
                Long id = Long.parseLong(arrayGeolocation[0]);
                if (measurePointRepository.existsById(id)) {
                    MeasurePoint measurePoint = measurePointRepository.getOne(id);
                    String lat = createGeolocation(arrayGeolocation[6]);
                    String lng = createGeolocation(arrayGeolocation[5]);
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

    public Page<MeasurePoint> measurePointPage(String name, String riverName, PageRequest pageRequest) {
        return measurePointRepository.findByPointNameOrRiverName(name, riverName, pageRequest);
    }

    public MeasurePoint getById(Long id) {
        Optional<MeasurePoint> optional = measurePointRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

//    public List<MeasurePoint> search(String input) {
//        List<MeasurePoint> measurePoints = new ArrayList<>();
//        measurePoints.addAll(tryFindMeasurePointByStringId(input));
//        input = "%" + input + "%";
//        measurePoints.addAll(measurePointRepository.findAllByPointNameIsLikeOrRiverNameIsLike(input, input));
//        return measurePoints;
//    }

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
            if (states.size() > 1) {
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

    public boolean existById(Long id) {
        return measurePointRepository.existsById(id);
    }

    private String createGeolocation(String input) {
        Double grades = Double.parseDouble(input.substring(0, 2));
        Double minutes = Double.parseDouble(input.substring(2, 4));
        Double seconds = Double.parseDouble(input.substring(4, 6));
        Double value = (seconds / 3600) + (minutes / 60) + grades;
        return value.toString();
    }

}
