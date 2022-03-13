package com.code.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberDto {
    @NotEmpty(message = "number cannot be empty")
    @Min(value = 5, message = "phone number cannot be less than 5")
    String number;
}
