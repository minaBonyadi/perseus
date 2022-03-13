package com.code.challenge.repository;

import com.code.challenge.entity.Email;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends CrudRepository<Email, Long> {
    Optional<Email> findEmailByMail(String email);
}
