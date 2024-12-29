package com.example.servizio.service.authentication;

import com.example.servizio.payload.SignupRequest;
import com.example.servizio.payload.UserDto;
import com.example.servizio.entity.User;
import com.example.servizio.enums.UserRole;
import com.example.servizio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    public UserDto signupClient(SignupRequest signupRequest) {
        if (signupRequest.getEmail() == null || signupRequest.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (signupRequest.getPassword() == null || signupRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        // Create a new User entity and set the properties
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setName(signupRequest.getName());
        user.setPhone(signupRequest.getPhone());
        user.setLastName(signupRequest.getLastName());
        user.setRole(UserRole.CLIENT);

        // Save the user to the repository
        return userRepository.save(user).getDto();
    }

    public Boolean presentByEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }

    public UserDto signupCompany(SignupRequest signupRequest) {
        if (signupRequest.getEmail() == null || signupRequest.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (signupRequest.getPassword() == null || signupRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        // Create a new User entity and set the properties
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setName(signupRequest.getName());
        user.setPhone(signupRequest.getPhone());
        user.setRole(UserRole.COMPANY);

        // Save the user to the repository
        return userRepository.save(user).getDto();
    }
}
