package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.model.MeasurePoint;
import com.sda.javagda25.boats.model.jsonDto.Feature;
import com.sda.javagda25.boats.model.jsonDto.FeatureCollection;
import com.sda.javagda25.boats.model.jsonDto.Geometry;
import com.sda.javagda25.boats.model.jsonDto.Properties;
import com.sda.javagda25.boats.repository.MeasurePointRepository;
import com.sda.javagda25.boats.service.MeasurePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feature/")
public class FeatureController {

    @Autowired
    private MeasurePointService measurePointService;

    @GetMapping("/{id}")
    public Feature getFeatureByMeasurePointId(@PathVariable(name = "id") Long id) {
        MeasurePoint measurePoint = measurePointService.getById(id);
        Feature feature = convertMeasurePointToFeature(measurePoint);
        return feature;
    }

    @GetMapping("/all")
    public FeatureCollection featureCollectionOfAllMeasurePoints() {
        List<MeasurePoint> allMeasurePoints = measurePointService.findAllMeasurePoints();
        int numberOfMeasurePoints = allMeasurePoints.size();
        Feature[] features = new Feature[numberOfMeasurePoints];
        for (int i = 0; i < numberOfMeasurePoints; i++) {
            MeasurePoint measurePoint = allMeasurePoints.get(i);
            if (measurePoint.getLat() != null && measurePoint.getLng() != null) {
                features[i] = convertMeasurePointToFeature(measurePoint);
            }
        }
        FeatureCollection featureCollection = new FeatureCollection();
        featureCollection.setFeautures(features);
        featureCollection.setType("FeatureCollection");
        return featureCollection;
    }


    private Feature convertMeasurePointToFeature(MeasurePoint measurePoint) {
        Geometry geometry = new Geometry();
        geometry.setType("Point");
        Double[] coordinates = {Double.parseDouble(measurePoint.getLat()), Double.parseDouble(measurePoint.getLng())};
        geometry.setCoordinates(coordinates);

        Properties properties = new Properties();
        properties.setMeasurePointId(measurePoint.getId().toString());
        properties.setMeasurePointName(measurePoint.getPointName());
        properties.setRiverName(measurePoint.getRiverName());

        Feature feature = new Feature();
        feature.setType("Feature");
        feature.setGeometry(geometry);
        feature.setProperties(properties);
        return feature;
    }

}
