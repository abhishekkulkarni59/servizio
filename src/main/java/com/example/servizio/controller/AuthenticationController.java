package com.example.servizio.controller;

import com.example.servizio.dto.AuthenticationRequest;
import com.example.servizio.dto.SignupRequestDTO;
import com.example.servizio.dto.UserDto;
import com.example.servizio.entity.User;
import com.example.servizio.enums.ApiErrorCode;
import com.example.servizio.repository.UserRepository;
import com.example.servizio.service.authentication.AuthService;
import com.example.servizio.service.jwt.UserDetailsServiceImpl;
import com.example.servizio.util.JwtUtil;
import com.example.servizio.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_STRING = "Authorization";

    @PostMapping("/client/sign-up")
    public ResponseEntity<?> signupClient(@RequestBody SignupRequestDTO signupRequestDTO) {

        // Validate that email and password are provided
        if (signupRequestDTO.getEmail() == null || signupRequestDTO.getEmail().isEmpty()) {
            return ResponseUtil.buildErrorResponse(
                    1005,
                    "Email is required.",
                    HttpStatus.BAD_REQUEST);
        }
        if (signupRequestDTO.getPassword() == null || signupRequestDTO.getPassword().isEmpty()) {
            return ResponseUtil.buildErrorResponse(
                    1006,
                    "Password is required.",
                    HttpStatus.BAD_REQUEST);
        }

        // Check if the client already exists
        if (authService.presentByEmail(signupRequestDTO.getEmail())) {
            return ResponseUtil.buildErrorResponse(
                    ApiErrorCode.CLIENT_EXISTS.getCode(),
                    ApiErrorCode.CLIENT_EXISTS.getMessage(),
                    HttpStatus.CONFLICT);
        }

        // Proceed with signup
        UserDto createdUser = authService.signupClient(signupRequestDTO);

        // Return a successful response with the created user
        return ResponseUtil.buildSuccessResponse(
                ApiErrorCode.SUCCESSFUL_CLIENT_SIGNUP.getCode(),
                ApiErrorCode.SUCCESSFUL_CLIENT_SIGNUP.getMessage(),
                createdUser,
                HttpStatus.CREATED);
    }

    @PostMapping("/company/sign-up")
    public ResponseEntity<?> signupCompany(@RequestBody SignupRequestDTO signupRequestDTO) {

        // Validate that email and password are provided
        if (signupRequestDTO.getEmail() == null || signupRequestDTO.getEmail().isEmpty()) {
            return ResponseUtil.buildErrorResponse(
                    1005,
                    "Email is required.",
                    HttpStatus.BAD_REQUEST);
        }
        if (signupRequestDTO.getPassword() == null || signupRequestDTO.getPassword().isEmpty()) {
            return ResponseUtil.buildErrorResponse(
                    1006,
                    "Password is required.",
                    HttpStatus.BAD_REQUEST);
        }

        // Check if the company already exists
        if (authService.presentByEmail(signupRequestDTO.getEmail())) {
            return ResponseUtil.buildErrorResponse(
                    ApiErrorCode.COMPANY_EXISTS.getCode(),
                    ApiErrorCode.COMPANY_EXISTS.getMessage(),
                    HttpStatus.CONFLICT);
        }

        // Proceed with signup
        UserDto createdUser = authService.signupCompany(signupRequestDTO);

        // Return a successful response with the created user
        return ResponseUtil.buildResponse(
                ApiErrorCode.SUCCESSFUL_COMPANY_SIGNUP.getCode(),
                ApiErrorCode.SUCCESSFUL_COMPANY_SIGNUP.getMessage(),
                createdUser);
    }

    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException, JSONException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            success(authenticationRequest,response);

        } catch (BadCredentialsException e) {
            log.error("###################################");
            failure(authenticationRequest,response);
//            throw new BadCredentialsException("Invalid username or password", e);

        }
    }

    private void success(AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException, JSONException {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        User user = userRepository.findFirstByEmail(authenticationRequest.getUsername());

        response.getWriter().write(new JSONObject()
                .put("userId", user.getId())
                .put("role", user.getRole())
                .put("code", ApiErrorCode.SUCCESSFUL_LOGIN.getCode())
                .put("message", ApiErrorCode.SUCCESSFUL_LOGIN.getMessage())
                .toString()
        );


        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader("Access-Control-Allow-Headers", "Authorization," +
                " X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-Header");

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
    }

    private void failure(AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException, JSONException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set status to 401
        response.setContentType("application/json");
        response.getWriter().write(new JSONObject()
                .put("code", 3102) // Custom error code
                .put("message", "Invalid username or password.")
                .toString());
    }
}
