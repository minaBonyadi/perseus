package com.code.challenge.controller;

import com.code.challenge.dto.UserDto;
import com.code.challenge.dto.rest_response.RestResponse;
import com.code.challenge.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    private ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(path = "/getById/{id}")
    private ResponseEntity<UserDto> getUserById(@PathVariable long id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(path = "/getByName")
    private ResponseEntity<UserDto> getUserByName(@RequestParam String firstName, @RequestParam String lastName){
        return new ResponseEntity<>(userService.getUserByName(firstName , lastName), HttpStatus.OK);
    }

    @PostMapping
    private void addAdditionalUserData(){

    }

    @PutMapping
    private void updateUserData(){

    }

    @DeleteMapping
    private void deleteUserData(){

    }
}
