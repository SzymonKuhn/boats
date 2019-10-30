package com.sda.javagda25.boats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasurePointState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer waterState;
    private LocalDateTime measureDateTime;

    private Long idStation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @Cascade(value = org.hibernate.annotations.CascadeType.DETACH)
    private MeasurePoint measurePoint;

    public MeasurePointState (MeasurePointDto measurePointDto) {
        this.waterState = measurePointDto.getStan_wody();
        if(measurePointDto.getStan_wody_data_pomiaru() != null ) {
            this.measureDateTime =  LocalDateTime.parse(measurePointDto.getStan_wody_data_pomiaru(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            this.measureDateTime = LocalDateTime.now();
        }
        this.idStation = measurePointDto.getId_stacji();
    }


}
