package com.sda.javagda25.boats.model.dto;

import com.sda.javagda25.boats.model.Boat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoatWithMinimumValueAndActualStateDto {
    private Boat boat;
    private Long measurePointId;
    private Integer minimumValue;
    private Integer warningValue;
}
