package com.devsu.bank.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovementDto {

    @NotNull(message = "account number cannot be null")
    @NotBlank(message = "type cannot be empty or whitespace")
    private String number;
    @NotNull(message = "type cannot be null")
    @NotBlank(message = "type cannot be empty or whitespace")
    private String type;
    @NotNull(message = "value cannot be null")
    private double amount;

}
