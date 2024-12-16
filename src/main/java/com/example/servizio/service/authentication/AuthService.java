package com.example.servizio.service.authentication;

import com.example.servizio.payload.SignupRequest;
import com.example.servizio.payload.UserDto;

public interface AuthService {

    public UserDto signupClient(SignupRequest signupRequest);
    public Boolean presentByEmail(String email);
    public UserDto signupCompany(SignupRequest signupRequest);

}
