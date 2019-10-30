package com.sda.javagda25.boats.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasurePointDto {
    private Long id_stacji;
    private String stacja;
    private String rzeka;
    private Integer stan_wody;
    private String stan_wody_data_pomiaru;
}
