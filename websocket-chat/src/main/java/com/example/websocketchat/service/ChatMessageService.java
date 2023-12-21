package com.example.websocketchat.service;

import com.example.websocketchat.entity.ChatMessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageService {

    @Autowired
    private ChatRoomService chatRoomService;

    /*public ChatMessageEntity send(ChatMessageEntity chatMessage){
        Long chatId = chatRoomService.findChatRoom(chatMessage.getSenderId(), chatMessage.getRecipientId());

    }*/
}
