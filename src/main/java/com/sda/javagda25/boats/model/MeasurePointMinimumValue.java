package com.sda.javagda25.boats.model;

import com.sun.xml.bind.v2.model.core.ID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasurePointMinimumValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer minimumValue;
    private Integer warningValue;

    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @Cascade(value = org.hibernate.annotations.CascadeType.DETACH)
    private MeasurePoint measurePoint;

    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @Cascade(value = org.hibernate.annotations.CascadeType.DETACH)
    private Boat boat;
}
