package com.gca.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class Trainee extends User {
    private Date dateOfBirth;
    private String address;
}
