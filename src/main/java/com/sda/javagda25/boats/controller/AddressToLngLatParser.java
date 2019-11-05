package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.model.AddressToLngLat;
import com.sda.javagda25.boats.model.dto.MeasurePointDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.List;
import java.util.Scanner;

@Controller
public class AddressToLngLatParser {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping ("/parse")
    public String parse () {
        String input = "Poznań";

        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=Poznań&key=AIzaSyDIlhefXdQwH7Eq9YFBHoIXDInMITZ264A";


        ResponseEntity<AddressToLngLat> respo = restTemplate.exchange(url, HttpMethod.GET, null, AddressToLngLat.class);
        AddressToLngLat body = respo.getBody();

        System.out.println(body);

//        AddressToLngLat body = addressToLngLatResponseEntity.getBody();


        return"redirect:/index";
    }
}
