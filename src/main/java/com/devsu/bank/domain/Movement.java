package com.devsu.bank.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "movement")
public class Movement {

    @Id
    private Integer id;
    @NotNull(message = "date cannot be null")
    private LocalDateTime transactionDate;
    @NotNull(message = "type cannot be null")
    @NotBlank(message = "type cannot be empty or whitespace")
    private String type;
    @NotNull(message = "value cannot be null")
    private double amount;
    @NotNull(message = "balance cannot be null")
    private double balance;
    @NotNull
    private Integer accountId;
}
