package com.devsu.bank.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    @NotNull(message = "number cannot be null")
    @NotBlank(message = "type cannot be empty or whitespace")
    private String number;
    @NotNull(message = "type cannot be null")
    @NotBlank(message = "type cannot be empty or whitespace")
    private String type;
    @NotNull(message = "initial_balance cannot be null")
    private double initialBalance;
    @NotNull(message = "status cannot be null")
    private boolean status;

}
