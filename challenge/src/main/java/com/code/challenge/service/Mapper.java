package com.code.challenge.service;

import com.code.challenge.dto.EmailDto;
import com.code.challenge.dto.PhoneNumberDto;
import com.code.challenge.dto.UserDto;
import com.code.challenge.entity.Email;
import com.code.challenge.entity.PhoneNumber;
import com.code.challenge.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Mapper {

    public Set<PhoneNumber> convertPhoneNumbersDtoToEntity(UserDto userDto) {
        return userDto.getPhoneNumbers().stream().map(phoneNumberDto ->
                PhoneNumber.builder().number(phoneNumberDto.getNumber()).build()).collect(Collectors.toSet());
    }

    public Set<Email> convertEmailsDtoToEntity(UserDto userDto) {
        return userDto.getEmails().stream().map(email -> Email.builder().mail(email.getMail()).build()).collect(Collectors.toSet());
    }

    public PhoneNumber convertPhoneNumbersDtoToEntity(PhoneNumberDto phoneNumberDto) {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setNumber(phoneNumberDto.getNumber());
        phoneNumber.setId(phoneNumber.getId());
        return phoneNumber;
    }

    public Email convertEmailsDtoToEntity(EmailDto emailDto) {
        Email email = new Email();
        email.setMail(emailDto.getMail());
        return email;
    }

    public Set<PhoneNumberDto> convertPhoneNumbersEntityToDto(Set<PhoneNumber> phoneNumbers) {
        return phoneNumbers.stream().map(phoneNumber -> PhoneNumberDto.builder().number(phoneNumber.getNumber()).build()).collect(Collectors.toSet());
    }

    public Set<EmailDto> convertEmailsEntityToDto(Set<Email> emails) {
        return emails.stream().map(email -> EmailDto.builder().mail(email.getMail()).build()).collect(Collectors.toSet());
    }

}
