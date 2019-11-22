package com.sda.javagda25.boats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String comment;
    private boolean isPublic;

    @Lob
    @Column (columnDefinition = "LONGBLOB")
    private byte[] photo;

    @ManyToMany (fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Set<CharacteristicPoint> characteristicPoints;

    @ManyToMany (fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Set<MeasurePoint> measurePoints;


}
