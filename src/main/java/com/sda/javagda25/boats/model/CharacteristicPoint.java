package com.sda.javagda25.boats.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacteristicPoint {

    @Id
    private Long id;

    private String name;
    private String comment;
    private boolean isPublic;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @Cascade(org.hibernate.annotations.CascadeType.DETACH)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Account account;


}
