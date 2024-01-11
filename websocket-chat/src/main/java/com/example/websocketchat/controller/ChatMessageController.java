package com.example.websocketchat.controller;

import com.example.websocketchat.entity.ChatMessageEntity;
import com.example.websocketchat.model.DTO.GetMessageDTOrequest;
import com.example.websocketchat.model.DTO.SendMessageDTOrequest;
import com.example.websocketchat.model.DTO.SendImgDTOresponse;
import com.example.websocketchat.model.DTO.SendTextDTOresponse;
import com.example.websocketchat.model.MessageType;
import com.example.websocketchat.service.ChatMessageService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Base64;
import java.util.List;

@Controller
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Transactional
    @MessageMapping("/messages/send/text")
    public void sendTextMessage(SendMessageDTOrequest request, Principal principal){
        chatMessageService.sendTextMessage(request.getMessage(), request.getChatId(), principal.getName());
        messagingTemplate.convertAndSend("/topic/messages/send/text/" + request.getChatId(), new SendTextDTOresponse(request.getMessage(), request.getChatId(), MessageType.TEXT, principal.getName()));
    }

    @Transactional
    @MessageMapping("/messages/send/img")
    public void sendImgMessage(@Payload String base64Image, @Header("chatId") String chatId, Principal principal) {
        System.out.println("Received image data: " + base64Image);
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        chatMessageService.sendImgMessage(imageBytes, chatId, principal.getName());
        messagingTemplate.convertAndSend("/topic/messages/send/img/" + chatId, new SendImgDTOresponse(principal.getName(), MessageType.IMG, imageBytes));
    }

    @Transactional
    @MessageMapping("/messages/get")
    public void getMessages(GetMessageDTOrequest request, Principal principal){
        List<ChatMessageEntity> messages = chatMessageService.getMessages(request.getChatId());
        messagingTemplate.convertAndSendToUser(principal.getName(),"/topic/messages/get", messages);
    }


}
