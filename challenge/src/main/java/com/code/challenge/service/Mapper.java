package com.code.challenge.service;

import com.code.challenge.dto.EmailDto;
import com.code.challenge.dto.PhoneNumberDto;
import com.code.challenge.entity.Email;
import com.code.challenge.entity.PhoneNumber;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Mapper {

    public Set<PhoneNumber> convertPhoneNumbersDtoToEntity(Set<PhoneNumberDto> phoneNumberDtoList) {
        Set<PhoneNumber> phoneNumbers = new HashSet<>();
        phoneNumberDtoList.forEach(phoneNumberDto -> phoneNumbers.add(PhoneNumber.builder().number(phoneNumberDto.getNumber()).build()));
        return phoneNumbers;
    }

    public Set<Email> convertEmailsDtoToEntity(Set<EmailDto> emailDtos) {
        Set<Email> emails = new HashSet<>();
        emailDtos.forEach(emailDto -> emails.add(Email.builder().mail(emailDto.getMail()).build()));
        return emails;
    }

    public Set<PhoneNumberDto> convertPhoneNumbersEntityToDto(Set<PhoneNumber> phoneNumbers) {
        Set<PhoneNumberDto> phoneNumberDtoList = new HashSet<>();
        phoneNumbers.forEach(phoneNumber -> phoneNumberDtoList.add(PhoneNumberDto.builder().number(phoneNumber.getNumber()).build()));
        return phoneNumberDtoList;
    }

    public Set<EmailDto> convertEmailsEntityToDto(Set<Email> emails) {
        return emails.stream().map(email -> EmailDto.builder().mail(email.getMail()).build()).collect(Collectors.toSet());
    }

}
