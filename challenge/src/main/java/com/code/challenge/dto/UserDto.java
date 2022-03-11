package com.code.challenge.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class UserDto {
    long id;
    String lastName;
    String firstName;
    List<EmailDto> emails;
    List<PhoneNumberDto> phoneNumbers;
}
