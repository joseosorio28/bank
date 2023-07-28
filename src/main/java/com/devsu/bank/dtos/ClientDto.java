package com.devsu.bank.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    private String name;
    private String gender;
    private int age;
    private String identificationNumber;
    private String address;
    private String phoneNumber;
    private String password;
    private boolean status;

}
