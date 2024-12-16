package com.example.servizio.payload;

import lombok.Data;

@Data
public class SignupRequest {

    private long id;

    private String email;
    private String password;
    private String name;
    private String lastName;
    private String phone;

}
