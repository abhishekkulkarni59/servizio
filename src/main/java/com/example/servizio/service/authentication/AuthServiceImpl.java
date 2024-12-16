package com.example.servizio.service.authentication;

import com.example.servizio.dto.SignupRequestDTO;
import com.example.servizio.dto.UserDto;
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

    public UserDto signupClient(SignupRequestDTO signupRequestDTO) {
        if (signupRequestDTO.getEmail() == null || signupRequestDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (signupRequestDTO.getPassword() == null || signupRequestDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        // Create a new User entity and set the properties
        User user = new User();
        user.setEmail(signupRequestDTO.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequestDTO.getPassword()));
        user.setName(signupRequestDTO.getName());
        user.setPhone(signupRequestDTO.getPhone());
        user.setLastName(signupRequestDTO.getLastName());
        user.setRole(UserRole.CLIENT);

        // Save the user to the repository
        return userRepository.save(user).getDto();
    }

    public Boolean presentByEmail(String email) {
        return userRepository.findFirstByEmail(email) != null;
    }

    public UserDto signupCompany(SignupRequestDTO signupRequestDTO) {
        if (signupRequestDTO.getEmail() == null || signupRequestDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (signupRequestDTO.getPassword() == null || signupRequestDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        // Create a new User entity and set the properties
        User user = new User();
        user.setEmail(signupRequestDTO.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequestDTO.getPassword()));
        user.setName(signupRequestDTO.getName());
        user.setPhone(signupRequestDTO.getPhone());
        user.setRole(UserRole.COMPANY);

        // Save the user to the repository
        return userRepository.save(user).getDto();
    }
}
