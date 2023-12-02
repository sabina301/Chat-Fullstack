package com.example.websocketchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@CrossOrigin("*")
public class PageController {
    @GetMapping("/login")
    public String showLoginPage() {
        return "login.html";
    }

    @GetMapping("/signup")
    public String showSignUpPage(){
        return "signup.html";
    }

    @GetMapping("/chat")
    public String showChatPage(){
        return "chat.html";
    }
}
