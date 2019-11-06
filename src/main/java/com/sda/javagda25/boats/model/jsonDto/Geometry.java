package com.sda.javagda25.boats.model.jsonDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Geometry {
    private Location location;
    private String type;
    private Double[] coordinates;
}