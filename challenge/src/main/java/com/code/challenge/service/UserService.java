package com.code.challenge.service;

import com.code.challenge.dto.EmailDto;
import com.code.challenge.dto.PhoneNumberDto;
import com.code.challenge.dto.UserDto;
import com.code.challenge.dto.rest_response.RestResponse;
import com.code.challenge.dto.rest_response.RestResponseType;
import com.code.challenge.entity.Email;
import com.code.challenge.entity.PhoneNumber;
import com.code.challenge.entity.User;
import com.code.challenge.exception.UserLogicalException;
import com.code.challenge.exception.UserNotFoundException;
import com.code.challenge.repository.EmailRepository;
import com.code.challenge.repository.PhoneNumberRepository;
import com.code.challenge.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
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

        User newUser = User.builder()
                    .firstName(userDto.getFirstName())
                    .lastName(userDto.getLastName())
                    .build();

        emailRepository.saveAll(emails);
        phoneNumberRepository.saveAll(phoneNumbers);

        newUser.setEmails(emails);
        newUser.setPhoneNumbers(phoneNumbers);

        userRepository.save(newUser);
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

        Optional<Email> updatedEmail = emailRepository.findEmailByMail(emailDto.getMail());

        if (updatedEmail.isPresent()) {
            updatedEmail.get().setMail(emailDto.getMail());
            emailRepository.save(updatedEmail.get());
        }else {
            emailRepository.save(email);
            emails.add(email);
            emails.addAll(user.getEmails());
            user.setEmails(emails);
        }

        User updatedUser = userRepository.save(user);
        log.info("A user by this first_name [{}] and last_name [{}] has just added/updated successfully", user.getFirstName(), user.getLastName());

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

        Optional<PhoneNumber> updatedPhoneNo = phoneNumberRepository.findPhoneNumberByNumber(phoneNumberDto.getNumber());

        if (updatedPhoneNo.isPresent()) {
            updatedPhoneNo.get().setNumber(phoneNumberDto.getNumber());
            phoneNumberRepository.save(updatedPhoneNo.get());
        }else {
            phoneNumberRepository.save(newPhoneNumber);
            phoneNumbers.add(newPhoneNumber);
            phoneNumbers.addAll(user.getPhoneNumbers());
            user.setPhoneNumbers(phoneNumbers);
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

    public RestResponse deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User Not Found!"));

        user.getEmails().forEach(Email::deleteEmails);
        user.getPhoneNumbers().forEach(PhoneNumber::deletePhoneNumber);

        emailRepository.deleteAll(user.getEmails());
        phoneNumberRepository.deleteAll(user.getPhoneNumbers());
        userRepository.delete(user);

        log.info("A user by this first_name [{}] and last_name [{}] has just deleted successfully", user.getFirstName(), user.getLastName());

        return new RestResponse(RestResponseType.SUCCESS, "deleting user done successfully!");
    }
}
