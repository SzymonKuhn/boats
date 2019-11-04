package com.sda.javagda25.boats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Base64;
import java.util.Set;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min = 4)
    private String username;

    @NotEmpty
    @Size(min = 4)
    private String password;

    private boolean isLocked;

    @ManyToMany (fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @Cascade(value = org.hibernate.annotations.CascadeType.DETACH)
    private Set<AccountRole> accountRoles;

    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String email;

    private String comment; //field only for admin, to comment account

    @Lob
    @Column (columnDefinition = "LONGBLOB")
    private byte[] photo;

    @OneToOne (fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Boat defaultBoat;


    public String getBase64StringPhoto () {
        return Base64.getEncoder().encodeToString(photo);
    }

}
