package com.devsu.bank.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovementDetailedDto {

    @NotNull(message = "date cannot be null")
    private LocalDateTime transactionDate;
    @NotNull(message = "type cannot be null")
    @NotBlank(message = "type cannot be empty or whitespace")
    private String type;
    @NotNull(message = "value cannot be null")
    private double amount;
    @NotNull(message = "balance cannot be null")
    private double balance;

}
