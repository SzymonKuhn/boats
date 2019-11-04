package com.sda.javagda25.boats.model.dto;

import com.sda.javagda25.boats.model.Boat;
import com.sda.javagda25.boats.model.MeasurePoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualAndMinimumStatesForBoatDto {

    private Boat boat;
    private MeasurePoint measurePoint;
    private Integer minimumValue;
    private Integer warningValue;
    private LocalDateTime measureDateTime;
    private Integer waterState;

}
