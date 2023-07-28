package com.devsu.bank.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {

    private LocalDateTime transactionDate;
    private String name;
    private String identificationNumber;
    private String number;
    private String type;
    private double initialBalance;
    private boolean status;
    private double amount;
    private double balance;

}
