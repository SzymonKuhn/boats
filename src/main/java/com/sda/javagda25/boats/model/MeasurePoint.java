package com.sda.javagda25.boats.model;

import com.sda.javagda25.boats.model.dto.MeasurePointDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasurePoint implements Point {
    @Id
    private Long id;

    private String pointName;

    private String riverName;

    private String lng;
    private String lat;

    public MeasurePoint(MeasurePointDto measurePointDto) {
        this.id = measurePointDto.getId_stacji();
        this.riverName = measurePointDto.getRzeka();
        this.pointName = measurePointDto.getStacja();
    }
}
