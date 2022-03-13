package com.code.challenge.controller;

import com.code.challenge.dto.EmailDto;
import com.code.challenge.dto.PhoneNumberDto;
import com.code.challenge.dto.UserDto;
import com.code.challenge.dto.rest_response.RestResponse;
import com.code.challenge.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "Create a new user", response = UserDto.class)
    @PostMapping(path ="/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get user by id", response = UserDto.class)
    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable long id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get user by specification like first name, last name", response = UserDto.class)
    @GetMapping(path = "/spec")
    public ResponseEntity<UserDto> getUserByName(@NonNull @RequestParam String firstName, @NonNull @RequestParam String lastName){
        return new ResponseEntity<>(userService.getUserByName(firstName , lastName), HttpStatus.OK);
    }

    @ApiOperation(value = "Add/Update user by email", response = UserDto.class)
    @PutMapping(path = "/{id}/email")
    public ResponseEntity<UserDto> addOrUpdateUserDataMail(@PathVariable long id, @Valid @RequestBody EmailDto emailDto){
        return new ResponseEntity<>(userService.addOrUpdateUserMail(id, emailDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Add/Update user by phone data", response = UserDto.class)
    @PutMapping(path = "/{id}/phone-number")
    public ResponseEntity<UserDto> addOrUpdateUserDataPhone(@PathVariable long id, @Valid @RequestBody PhoneNumberDto phoneNumberDto){
        return new ResponseEntity<>(userService.addOrUpdateUserPhoneNo(id, phoneNumberDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete whole user data", response = UserDto.class)
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<RestResponse> deleteUser(@PathVariable long id) {
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }
}
