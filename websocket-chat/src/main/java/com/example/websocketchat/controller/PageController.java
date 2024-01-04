package com.example.websocketchat.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
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
    public String showChatPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
        return "chat.html";
    }

    @GetMapping("/chatroom/{id}")
    public String selectRoom(@PathVariable("id") Long id){
        return "oneChat.html";
    }
}
