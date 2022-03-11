package com.code.challenge.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "EMAIL")
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
