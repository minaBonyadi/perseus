package com.code.challenge.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@Table(name = "USER", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"LAST_NAME", "FIRST_NAME"})
})
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "LAST_NAME")
    String lastName;

    @Column(name = "FIRST_NAME")
    String firstName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    Set<Email> emails;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    Set<PhoneNumber> phoneNumbers;
}
