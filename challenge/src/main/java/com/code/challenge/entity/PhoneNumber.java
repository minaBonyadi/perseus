package com.code.challenge.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "PHONE_NUMBER")
@NoArgsConstructor
public class PhoneNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    int id;

    @Column(name = "NUMBER")
    String number;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    User user;
}
