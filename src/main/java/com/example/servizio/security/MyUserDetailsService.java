package com.example.servizio.security;

import com.example.servizio.exception.ResourceNotFoundException;
import com.example.servizio.entity.User;
import com.example.servizio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findFirstByEmail(username).orElseThrow(()-> new ResourceNotFoundException(String.format("%s not found with %s", "User", "Email : " + username)));
        return new UserPrincipal(user);

    }

}
