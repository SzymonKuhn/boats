package com.sda.javagda25.boats.model;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.Base64;
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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @Cascade(org.hibernate.annotations.CascadeType.DETACH)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Account account;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photo;

    @Lob
    @Column (columnDefinition = "LONGBLOB")
    private byte[] thumbnail;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Set<CharacteristicPoint> characteristicPoints;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Set<MeasurePoint> measurePoints;

    public String getBase64Thumbnail() {
        return Base64.getEncoder().encodeToString(thumbnail);
    }

    public String getBase64StringPhoto () {
        return Base64.getEncoder().encodeToString(photo);
    }
}
