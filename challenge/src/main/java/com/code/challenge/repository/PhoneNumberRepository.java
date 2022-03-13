package com.code.challenge.repository;

import com.code.challenge.entity.PhoneNumber;
import com.code.challenge.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneNumberRepository extends CrudRepository<PhoneNumber, Long> {
    Optional<PhoneNumber> findPhoneNumberByNumberAndUser(String number, User user);
}
