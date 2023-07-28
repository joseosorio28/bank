package com.devsu.bank.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    private Integer id;
    @NotBlank(message = "type cannot be empty or whitespace")
    @NotNull(message = "number cannot be null")
    private String number;
    @NotNull(message = "type cannot be null")
    @NotBlank(message = "type cannot be empty or whitespace")
    private String type;
    @NotNull(message = "initial_balance cannot be null")
    private double initialBalance;
    private boolean status;
    @NotNull
    private Integer clientId;

}
