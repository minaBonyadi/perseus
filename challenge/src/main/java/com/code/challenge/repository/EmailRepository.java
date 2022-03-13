package com.code.challenge.repository;

import com.code.challenge.entity.Email;
import com.code.challenge.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends CrudRepository<Email, Long> {
    Optional<Email> findEmailByMailAndUser(String email, User user);
}
