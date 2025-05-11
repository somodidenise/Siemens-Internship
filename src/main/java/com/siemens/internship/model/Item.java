package com.siemens.internship.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    //The ids will be unique and generated automatically
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //Name must not be null
    @NotBlank(message = "Name can't be blank")
    private String name;

    private String description;

    private String status;

    //Email must not be null and also match a valid format
    @NotBlank(message = "Email can't be blank")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,})+$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Invalid email")
    private String email;
}