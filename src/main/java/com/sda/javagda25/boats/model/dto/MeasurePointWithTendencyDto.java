package com.sda.javagda25.boats.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasurePointWithTendencyDto {
    private Long id;
    private String pointName;
    private String riverName;
    private String lng;
    private String lat;
    private Boolean upTendency;
    private Boolean downTendency;
}
