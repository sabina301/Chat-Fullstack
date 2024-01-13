package com.example.websocketchat.controller;

import com.example.websocketchat.service.ChatRoomService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;


@Controller
public class PageController {

    @Autowired
    private ChatRoomService chatRoomService;

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

    @Transactional
    @GetMapping("/chatroom/{id}")
    public String selectRoom(@PathVariable("id") Long chatId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (chatRoomService.userHereByUsername(username,chatId)){
            return "oneChat.html";
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No access");
        }

    }

}
