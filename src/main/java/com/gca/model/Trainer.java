package com.gca.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Trainer extends User{
    private String specialization;
    private User user;
}
