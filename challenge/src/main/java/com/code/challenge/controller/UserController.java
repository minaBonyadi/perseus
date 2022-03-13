package com.code.challenge.controller;

import com.code.challenge.dto.EmailDto;
import com.code.challenge.dto.PhoneNumberDto;
import com.code.challenge.dto.UserDto;
import com.code.challenge.service.UserService;
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

    @PostMapping(path ="/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable long id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/spec")
    public ResponseEntity<UserDto> getUserByName(@NonNull @RequestParam String firstName, @NonNull @RequestParam String lastName){
        return new ResponseEntity<>(userService.getUserByName(firstName , lastName), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/emails")
    public ResponseEntity<UserDto> addOrUpdateUserDataMail(@PathVariable long id, @Valid @RequestBody EmailDto emailDto){
        return new ResponseEntity<>(userService.addOrUpdateUserMail(id, emailDto), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/phone-numbers")
    public ResponseEntity<UserDto> addOrUpdateUserDataPhone(@PathVariable long id, @Valid @RequestBody PhoneNumberDto phoneNumberDto){
        return new ResponseEntity<>(userService.addOrUpdateUserPhoneNo(id, phoneNumberDto), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable long id){
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }
}
