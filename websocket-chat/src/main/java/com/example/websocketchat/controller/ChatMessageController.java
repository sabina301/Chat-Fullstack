package com.example.websocketchat.controller;

import com.example.websocketchat.entity.ChatMessageEntity;
import com.example.websocketchat.model.DTO.GetMessageDTOrequest;
import com.example.websocketchat.model.DTO.SendMessageDTOrequest;
import com.example.websocketchat.model.DTO.SendMessageDTOresponse;
import com.example.websocketchat.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/messages/send")
    public void sendMessage(SendMessageDTOrequest request, Principal principal){
        chatMessageService.sendMessage(request.getMessage(), request.getChatId(), principal.getName());
        messagingTemplate.convertAndSend("/topic/messages/send/" + request.getChatId(), new SendMessageDTOresponse(request.getMessage(), principal.getName()));
    }

    @MessageMapping("/messages/get")
    public void getMessages(GetMessageDTOrequest request, Principal principal){
        List<ChatMessageEntity> messages = chatMessageService.getMessages(request.getChatId());
        messagingTemplate.convertAndSendToUser(principal.getName(),"/topic/messages/get", messages);
    }
}
