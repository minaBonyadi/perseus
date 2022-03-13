package com.code.challenge.component;


import com.code.challenge.dto.EmailDto;
import com.code.challenge.dto.PhoneNumberDto;
import com.code.challenge.dto.UserDto;
import com.code.challenge.entity.Email;
import com.code.challenge.entity.PhoneNumber;
import com.code.challenge.entity.User;
import com.code.challenge.repository.EmailRepository;
import com.code.challenge.repository.PhoneNumberRepository;
import com.code.challenge.repository.UserRepository;
import com.code.challenge.service.Mapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
public class UserManagement {

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PhoneNumberRepository phoneNumberRepository;
    private final Mapper mapper;

    /**
     *
     * @param userDto with all data
     *  create a new user, save it on db then send it to a service
     */
    public User getNewUser(UserDto userDto) {
        Set<Email> emails = mapper.convertEmailsDtoToEntity(userDto);
        Set<PhoneNumber> phoneNumbers = mapper.convertPhoneNumbersDtoToEntity(userDto);

        User newUser = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .build();

        emailRepository.saveAll(emails);
        phoneNumberRepository.saveAll(phoneNumbers);

        newUser.setEmails(emails);
        newUser.setPhoneNumbers(phoneNumbers);

        return userRepository.save(newUser);
    }

    /**
     *
     * @param emailDto with their data
     * @param user with their data
     *  create or update an email, save it on db then send it to a service
     */
    public User getUserByEmailDto(EmailDto emailDto, User user) {
        Set<Email> emails;
        Email email = mapper.convertEmailsDtoToEntity(emailDto);

        Optional<Email> updatedEmail = emailRepository.findEmailByMailAndUser(emailDto.getMail(), user);

        if (updatedEmail.isPresent()) {
            updatedEmail.get().setMail(emailDto.getMail());
            emailRepository.save(updatedEmail.get());
        }else {
            emailRepository.save(email);
            user.getEmails().add(email);
            emails = user.getEmails();
            user.setEmails(emails);
        }

        return userRepository.save(user);
    }

    /**
     *
     * @param phoneNumberDto with their data
     * @param user with their data
     *  create or update an email, save it on db then send it to a service
     */
    public User getUserByPhoneNumber(PhoneNumberDto phoneNumberDto, User user) {
        Set<PhoneNumber> phoneNumbers;
        PhoneNumber newPhoneNumber = mapper.convertPhoneNumbersDtoToEntity(phoneNumberDto);

        Optional<PhoneNumber> updatedPhoneNo = phoneNumberRepository.findPhoneNumberByNumberAndUser(phoneNumberDto.getNumber(), user);

        if (updatedPhoneNo.isPresent()) {
            updatedPhoneNo.get().setNumber(phoneNumberDto.getNumber());
            phoneNumberRepository.save(updatedPhoneNo.get());
        }else {
            phoneNumberRepository.save(newPhoneNumber);
            user.getPhoneNumbers().add(newPhoneNumber);
            phoneNumbers = user.getPhoneNumbers();
            user.setPhoneNumbers(phoneNumbers);
        }

        return userRepository.save(user);
    }
}
