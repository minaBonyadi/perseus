package com.code.challenge.entity;

import lombok.*;

import javax.persistence.*;

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
    long id;

    @Column(name = "MAIL")
    String mail;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    User user;

    public void deleteEmails() {
        if (user.getEmails() != null) {
            user.getEmails().remove(this);
        }
    }
}
