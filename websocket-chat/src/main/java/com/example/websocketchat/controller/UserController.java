package com.example.websocketchat.controller;

import com.example.websocketchat.entity.UserEntity;
import com.example.websocketchat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @MessageMapping("/user/add")
    @SendTo("/user/public")
    public ResponseEntity<String> addUser(@Payload UserEntity user){
        userService.addUser(user);
        return ResponseEntity.ok("Add: user");
    }

    @MessageMapping("/user/disconnect")
    @SendTo("/user/public")
    public ResponseEntity<String> disconnectUser(@Payload UserEntity user){
        userService.disconnectUser(user);
        return ResponseEntity.ok("Disconnect: user");
    }

    @GetMapping("/users/connected")
    public ResponseEntity<List<UserEntity>> findConnectedUsers(){
        return ResponseEntity.ok(userService.findConnectedUsers());
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<UserEntity>> findAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }
}
