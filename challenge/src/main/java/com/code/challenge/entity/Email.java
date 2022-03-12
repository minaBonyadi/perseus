package com.code.challenge.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@Entity
@Table(name = "EMAIL")
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "MAIL")
    String mail;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    User user;
}
