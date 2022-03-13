package com.code.challenge.service;

import com.code.challenge.component.UserManagement;
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
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserService implements UserCrudHandler {

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PhoneNumberRepository phoneNumberRepository;
    private final Mapper mapper;
    private final UserManagement userManagement;

    /**
     *
     * @param userDto with their data
     *  create a user and return userDto
     */
    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByFirstNameAndLastNameIgnoreCase(userDto.getFirstName(), userDto.getLastName()).isPresent()) {
            throw new UserLogicalException("User is already exists!");
        }
        User newUser = userManagement.getNewUser(userDto);

        log.info("A user by this first_name [{}] and last_name [{}] has just added successfully", userDto.getFirstName(), userDto.getLastName());

        return mapper.mapUserEntityToUserDto(newUser);
    }

    /**
     *
     * @param userId with long id
     *  find a user and return it as a userDto
     */
    public UserDto getUserById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User Not Found!"));

        return mapper.mapUserEntityToUserDto(user);
    }

    /**
     *
     * @param firstName string name
     * @param lastName string last name
     *  create or update an email, save it on db then send it to a service
     */
    public UserDto getUserByName(String firstName, String lastName) {
        User user = userRepository.findByFirstNameAndLastNameIgnoreCase(firstName, lastName)
                .orElseThrow(()-> new UserNotFoundException("User Not Found!"));

        return mapper.mapUserEntityToUserDto(user);
    }

    /**
     *
     * @param id long
     * @param emailDto with their data
     *  add or update an email, save it on db then send it to a service
     */
    public UserDto addOrUpdateUserMail(long id, EmailDto emailDto) {

        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User Not Found!"));
        User updatedUser = userManagement.getUserByEmailDto(emailDto, user);
        log.info("A user by this first_name [{}] and last_name [{}] has just added/updated successfully", user.getFirstName(), user.getLastName());

        return mapper.mapUserEntityToUserDto(updatedUser);
    }

    /**
     * @param userId long
     * @param phoneNumberDto with their data
     *  create or update an phone number, save it on db then send it to a service
     */
    public UserDto addOrUpdateUserPhoneNo(long userId, PhoneNumberDto phoneNumberDto) {

        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User Not Found!"));
        User updatedUser = userManagement.getUserByPhoneNumber(phoneNumberDto, user);

        log.info("A user by this first_name [{}] and last_name [{}] has just updated successfully", user.getFirstName(), user.getLastName());

        return mapper.mapUserEntityToUserDto(updatedUser);
    }

    /**
     *
     * @param userId with their data
     *  create or update an email, save it on db then send it to a service
     */
    public RestResponse deleteUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User Not Found!"));

        user.getEmails().forEach(Email::deleteEmails);
        user.getPhoneNumbers().forEach(PhoneNumber::deletePhoneNumber);

        emailRepository.deleteAll(user.getEmails());
        phoneNumberRepository.deleteAll(user.getPhoneNumbers());
        userRepository.delete(user);

        log.info("A user by this first_name [{}] and last_name [{}] has just deleted successfully", user.getFirstName(), user.getLastName());

        return new RestResponse(RestResponseType.SUCCESS, "deleting user done successfully!");
    }

}
