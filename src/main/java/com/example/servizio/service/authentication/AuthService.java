package com.example.servizio.service.authentication;

import com.example.servizio.dto.SignupRequestDTO;
import com.example.servizio.dto.UserDto;

public interface AuthService {

    public UserDto signupClient(SignupRequestDTO signupRequestDTO);
    public Boolean presentByEmail(String email);
    public UserDto signupCompany(SignupRequestDTO signupRequestDTO);

}
