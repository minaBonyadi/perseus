package com.code.challenge.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@Builder
public class UserDto {
    long id;
    @NotEmpty(message = "last name cannot be empty")
    String lastName;
    @NotEmpty(message = "first name cannot be empty")
    String firstName;
    Set<EmailDto> emails;
    Set<PhoneNumberDto> phoneNumbers;
}
