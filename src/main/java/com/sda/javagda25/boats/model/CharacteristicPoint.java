package com.sda.javagda25.boats.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Base64;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacteristicPoint implements Point{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String comment;
    private boolean isPublic;

    private String lng;
    private String lat;

    @Enumerated(EnumType.ORDINAL)
    private PointCategory pointCategory;

    @Lob
    @Column (columnDefinition = "LONGBLOB")
    private byte[] photo;

    @Lob
    @Column (columnDefinition = "LONGBLOB")
    private byte[] thumbnail;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @Cascade(org.hibernate.annotations.CascadeType.DETACH)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Account account;

    public String getBase64Thumbnail() {
        return Base64.getEncoder().encodeToString(thumbnail);
    }
}
