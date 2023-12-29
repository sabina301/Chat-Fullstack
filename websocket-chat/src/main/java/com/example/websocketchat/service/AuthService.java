package com.example.websocketchat.service;


import com.example.websocketchat.entity.UserEntity;
import com.example.websocketchat.exception.RoleNotFoundException;
import com.example.websocketchat.exception.UserNotFoundException;
import com.example.websocketchat.model.DTO.LoginResponseDTO;
import com.example.websocketchat.model.DTO.RegisterResponseDTO;
import com.example.websocketchat.model.Role;
import com.example.websocketchat.repository.RoleRepository;
import com.example.websocketchat.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
@Transactional
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;
    public RegisterResponseDTO registerUser(String username, String password){
        if (userRepository.findByUsername(username).isEmpty()){
            String encodedPassword = passwordEncoder.encode(password);
            Role userRole = roleRepository.findByAuthority("USER").orElseThrow(() -> new RoleNotFoundException("Role is not found"));
            Set<Role> authorities = new HashSet<Role>();
            authorities.add(userRole);

            UserEntity userEntity = new UserEntity(username, encodedPassword, authorities);
            userRepository.save(userEntity);
            return new RegisterResponseDTO(username);
        } else {
            return new RegisterResponseDTO(null);
        }
    }


    public LoginResponseDTO loginUser(String username, String password){

        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            String token = tokenService.generateJwt(auth);
            return new LoginResponseDTO(userRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException("User is not found")), token);

        } catch(Exception e){
            return new LoginResponseDTO(null, "");
        }
    }
}

