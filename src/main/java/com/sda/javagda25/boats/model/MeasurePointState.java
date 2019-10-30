package com.sda.javagda25.boats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasurePoint {
    @Id
    private Long id_stacji;

    private String stacja;

    private String rzeka;

}
