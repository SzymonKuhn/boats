package com.sda.javagda25.boats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Boat {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;
    private Integer immersion;
    private String comment;

    @Lob
    @Column (columnDefinition = "LONGBLOB")
    private byte[] photo;

    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @Cascade(org.hibernate.annotations.CascadeType.DETACH)
    private Account account;
}
