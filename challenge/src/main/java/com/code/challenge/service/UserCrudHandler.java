package com.code.challenge.service;

import com.code.challenge.dto.EmailDto;
import com.code.challenge.dto.PhoneNumberDto;
import com.code.challenge.dto.UserDto;
import com.code.challenge.dto.rest_response.RestResponse;

public interface UserCrudHandler {

    UserDto createUser(UserDto userDto);

    UserDto getUserById(long userId);

    UserDto getUserByName(String firstName, String lastName);

    UserDto addOrUpdateUserMail(long id, EmailDto emailDto);

    UserDto addOrUpdateUserPhoneNo(long userId, PhoneNumberDto phoneNumberDto);

    RestResponse deleteUser(long userId);

}
