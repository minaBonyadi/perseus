package com.code.challenge.controller;

import com.code.challenge.dto.EmailDto;
import com.code.challenge.dto.PhoneNumberDto;
import com.code.challenge.dto.UserDto;
import com.code.challenge.dto.rest_response.RestResponse;
import com.code.challenge.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/user")
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

    @PutMapping(path = "/{id}")
    public ResponseEntity<UserDto> updateUserDataMailOrPhone(@PathVariable long id, @Nullable @RequestBody EmailDto emailDto,
                                                               @Nullable @RequestBody PhoneNumberDto phoneNumberDto){
        return new ResponseEntity<>(userService.updateUserMailOrPhoneNo(id, emailDto, phoneNumberDto), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable long id){
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }
}
