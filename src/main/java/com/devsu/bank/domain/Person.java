package com.devsu.bank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table("person")
public class Person {

    @Id
    @Column("id")
    private Integer id;
    @Column("name")
    private String name;
    @Column("gender")
    private String gender;
    @Column("age")
    private Integer age;
    @Column("identification_number")
    private String identificationNumber;
    @Column("address")
    private String address;
    @Column("phone_number")
    private String phoneNumber;

}
