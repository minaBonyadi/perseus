package com.code.challenge.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class UserDto {
    long id;
    String lastName;
    String firstName;
    Set<EmailDto> emails;
    Set<PhoneNumberDto> phoneNumbers;
}
