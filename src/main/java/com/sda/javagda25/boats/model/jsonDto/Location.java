package com.sda.javagda25.boats.model.jsonDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private String lat;
    private String lng;
}
