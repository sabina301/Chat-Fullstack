package com.example.websocketchat.controller;

import com.example.websocketchat.model.DTO.LoginResponseDTO;
import com.example.websocketchat.model.DTO.RegisterResponseDTO;
import com.example.websocketchat.model.DTO.RegistrationDTO;
import com.example.websocketchat.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private AuthService authenticationService;

    @PostMapping("/register")
    private RegisterResponseDTO userRegister(@RequestBody RegistrationDTO registrationDTO) throws Exception {
        return authenticationService.registerUser(registrationDTO.getUsername(), registrationDTO.getPassword());
    }

    @PostMapping("/login")
    public LoginResponseDTO loginUser(@RequestBody RegistrationDTO body){
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }
}