package com.code.challenge.service;

import com.code.challenge.dto.EmailDto;
import com.code.challenge.dto.PhoneNumberDto;
import com.code.challenge.dto.UserDto;
import com.code.challenge.entity.Email;
import com.code.challenge.entity.PhoneNumber;
import com.code.challenge.entity.User;
import com.code.challenge.exception.EmailNotFoundException;
import com.code.challenge.exception.PhoneNumberNotFoundException;
import com.code.challenge.exception.UserLogicalException;
import com.code.challenge.exception.UserNotFoundException;
import com.code.challenge.repository.EmailRepository;
import com.code.challenge.repository.PhoneNumberRepository;
import com.code.challenge.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PhoneNumberRepository phoneNumberRepository;
    private final Mapper mapper;

    public UserDto createUser(UserDto userDto){
        if (userRepository.findByFirstNameAndLastNameIgnoreCase(userDto.getFirstName(), userDto.getLastName()).isPresent()) {
            throw new UserLogicalException("User is already exists!");
        }
        Set<Email> emails = mapper.convertEmailsDtoToEntity(userDto);
        Set<PhoneNumber> phoneNumbers = mapper.convertPhoneNumbersDtoToEntity(userDto);

        User newUser = userRepository.save(User.builder()
                    .firstName(userDto.getFirstName())
                    .lastName(userDto.getLastName())
                    .build());

        emails.forEach(email -> emailRepository.save(email).setUser(newUser));
        phoneNumbers.forEach(phoneNumber -> phoneNumberRepository.save(phoneNumber).setUser(newUser));

        log.info("A user by this first_name [{}] and last_name [{}] has just added successfully", userDto.getFirstName(), userDto.getLastName());

        return UserDto.builder()
                .id(newUser.getId())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .emails(mapper.convertEmailsEntityToDto(newUser.getEmails()))
                .phoneNumbers(mapper.convertPhoneNumbersEntityToDto(newUser.getPhoneNumbers()))
                .build();
    }

    public UserDto getUserById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User Not Found!"));

        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .emails(mapper.convertEmailsEntityToDto(user.getEmails()))
                .phoneNumbers(mapper.convertPhoneNumbersEntityToDto(user.getPhoneNumbers()))
                .build();
    }

    public UserDto getUserByName(String firstName, String lastName) {
        User user = userRepository.findByFirstNameAndLastNameIgnoreCase(firstName, lastName)
                .orElseThrow(()-> new UserNotFoundException("User Not Found!"));

        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .emails(mapper.convertEmailsEntityToDto(user.getEmails()))
                .phoneNumbers(mapper.convertPhoneNumbersEntityToDto(user.getPhoneNumbers()))
                .build();
    }

    public UserDto addOrUpdateUserMail(long id, EmailDto emailDto) {

        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User Not Found!"));
        Set<Email> emails = new HashSet<>();
        Email email = mapper.convertEmailsDtoToEntity(emailDto);

        if (emailDto.getId() == 0) {
            emailRepository.save(email);
            emails.add(email);
            emails.addAll(user.getEmails());
            user.setEmails(emails);
        }else {
            Email updatedEmail = emailRepository.findById(emailDto.getId()).orElseThrow(()-> new EmailNotFoundException("Email Not Found!"));
            updatedEmail.setMail(emailDto.getMail());
            emailRepository.save(updatedEmail);
        }

        User updatedUser = userRepository.save(user);
        log.info("A user by this first_name [{}] and last_name [{}] has just updated successfully", user.getFirstName(), user.getLastName());

        return UserDto.builder()
                .id(updatedUser.getId())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .emails(mapper.convertEmailsEntityToDto(updatedUser.getEmails()))
                .phoneNumbers(mapper.convertPhoneNumbersEntityToDto(updatedUser.getPhoneNumbers()))
                .build();
    }

    public UserDto addOrUpdateUserPhoneNo(long id, PhoneNumberDto phoneNumberDto) {

        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User Not Found!"));
        Set<PhoneNumber> phoneNumbers = new HashSet<>();
        PhoneNumber newPhoneNumber = mapper.convertPhoneNumbersDtoToEntity(phoneNumberDto);

        if (phoneNumberDto.getId() == 0) {

            phoneNumberRepository.save(newPhoneNumber);
            phoneNumbers.add(newPhoneNumber);
            phoneNumbers.addAll(user.getPhoneNumbers());
            user.setPhoneNumbers(phoneNumbers);

        }else {
            PhoneNumber updatedPhoneNo = phoneNumberRepository.findById(phoneNumberDto.getId()).orElseThrow(()->
                    new PhoneNumberNotFoundException("Phone number Not Found!"));
            updatedPhoneNo.setNumber(phoneNumberDto.getNumber());
            phoneNumberRepository.save(updatedPhoneNo);
        }

        User updatedUser = userRepository.save(user);

        log.info("A user by this first_name [{}] and last_name [{}] has just updated successfully", user.getFirstName(), user.getLastName());

        return UserDto.builder()
                .id(updatedUser.getId())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .emails(mapper.convertEmailsEntityToDto(updatedUser.getEmails()))
                .phoneNumbers(mapper.convertPhoneNumbersEntityToDto(updatedUser.getPhoneNumbers()))
                .build();
    }

    public UserDto deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User Not Found!"));

        userRepository.delete(user);
        log.info("A user by this first_name [{}] and last_name [{}] has just deleted successfully", user.getFirstName(), user.getLastName());

        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .emails(mapper.convertEmailsEntityToDto(user.getEmails()))
                .phoneNumbers(mapper.convertPhoneNumbersEntityToDto(user.getPhoneNumbers()))
                .build();
    }
}
