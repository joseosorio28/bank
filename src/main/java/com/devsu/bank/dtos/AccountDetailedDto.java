package com.devsu.bank.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountDetailedDto extends AccountDto {

    @NotNull(message = "client identification number cannot be null")
    @NotBlank(message = "type cannot be empty or whitespace")
    private String identificationNumber;

}
