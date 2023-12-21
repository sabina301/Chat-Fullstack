package com.example.websocketchat.controller;

import com.example.websocketchat.entity.ChatMessageEntity;
import com.example.websocketchat.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    /*@MessageMapping("/chat")
    public void sendMessage(@Payload ChatMessageEntity chatMessage){
        ChatMessageEntity savedChatMessage = chatMessageService.send(chatMessage);
    }*/
}
