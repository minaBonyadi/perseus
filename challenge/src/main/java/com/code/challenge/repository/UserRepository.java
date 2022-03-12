package com.code.challenge.repository;

import com.code.challenge.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByFirstNameAndLastNameIgnoreCase(String firstName, String lastName);
}
