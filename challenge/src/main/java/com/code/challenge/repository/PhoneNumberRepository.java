package com.code.challenge.repository;

import com.code.challenge.entity.PhoneNumber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneNumberRepository extends CrudRepository<PhoneNumber, Long> {
    Optional<PhoneNumber> findPhoneNumberByNumber(String number);
}
