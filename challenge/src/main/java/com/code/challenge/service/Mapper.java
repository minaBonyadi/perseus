package com.code.challenge.service;

import com.code.challenge.dto.EmailDto;
import com.code.challenge.dto.PhoneNumberDto;
import com.code.challenge.entity.Email;
import com.code.challenge.entity.PhoneNumber;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Mapper {

    public List<PhoneNumber> convertPhoneNumbersDtoToEntity(List<PhoneNumberDto> phoneNumberDtoList) {
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumberDtoList.forEach(phoneNumberDto -> phoneNumbers.add(PhoneNumber.builder().number(phoneNumberDto.getNumber()).build()));
        return phoneNumbers;
    }

    public List<Email> convertEmailsDtoToEntity(List<EmailDto> emailDtos) {
        List<Email> emails = new ArrayList<>();
        emailDtos.forEach(emailDto -> emails.add(Email.builder().mail(emailDto.getMail()).build()));
        return emails;
    }

    public List<PhoneNumberDto> convertPhoneNumbersEntityToDto(List<PhoneNumber> phoneNumbers) {
        List<PhoneNumberDto> phoneNumberDtoList = new ArrayList<>();
        phoneNumbers.forEach(phoneNumber -> phoneNumberDtoList.add(PhoneNumberDto.builder().number(phoneNumber.getNumber()).build()));
        return phoneNumberDtoList;
    }

    public List<EmailDto> convertEmailsEntityToDto(List<Email> emails) {
        List<EmailDto> emailDtoList = new ArrayList<>();
        emails.forEach(email -> emailDtoList.add(EmailDto.builder().mail(email.getMail()).build()));
        return emailDtoList;
    }

}
