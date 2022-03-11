package com.code.challenge.service;

import com.code.challenge.dto.EmailDto;
import com.code.challenge.dto.PhoneNumberDto;
import com.code.challenge.dto.UserDto;
import com.code.challenge.entity.Email;
import com.code.challenge.entity.PhoneNumber;
import com.code.challenge.entity.User;
import com.code.challenge.exception.UserLogicalException;
import com.code.challenge.exception.UserNotFoundException;
import com.code.challenge.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Mapper mapper;

    public UserDto createUser(UserDto userDto) {
        userRepository.findByFirstNameAndLastName(userDto.getFirstName(), userDto.getLastName())
                .orElseThrow(() -> new UserLogicalException("User has already exists"));

        User newUser = userRepository.save(User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .emails(mapper.convertEmailsDtoToEntity(userDto.getEmails()))
                .phoneNumbers(mapper.convertPhoneNumbersDtoToEntity(userDto.getPhoneNumbers()))
                .build());

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
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .emails(mapper.convertEmailsEntityToDto(user.getEmails()))
                .phoneNumbers(mapper.convertPhoneNumbersEntityToDto(user.getPhoneNumbers()))
                .build();
    }

    public UserDto getUserByName(String firstName, String lastName) {
        User user = userRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(UserNotFoundException::new);

        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .emails(mapper.convertEmailsEntityToDto(user.getEmails()))
                .phoneNumbers(mapper.convertPhoneNumbersEntityToDto(user.getPhoneNumbers()))
                .build();
    }

//    User user = userRepository.findById(userDto.getId()).orElseThrow(UserNotFoundException::new);

}
