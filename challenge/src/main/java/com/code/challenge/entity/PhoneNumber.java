package com.code.challenge.entity;

import jdk.jfr.Event;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "PHONE_NUMBER")
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    long id;

    @Column(name = "NUMBER")
    String number;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    User user;

    public void deletePhoneNumber() {
        if (user.getPhoneNumbers() != null) {
            user.getPhoneNumbers().remove(this);
        }
    }
}
