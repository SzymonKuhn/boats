package com.sda.javagda25.boats.model.jsonDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feature {
    private String type;
    private Geometry geometry;
    private Properties properties;
}
