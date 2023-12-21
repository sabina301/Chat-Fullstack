package com.example.websocketchat.controller;

import com.example.websocketchat.entity.ChatRoomEntity;
import com.example.websocketchat.model.DTO.ChatRoomDTOrequest;
import com.example.websocketchat.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
public class ChatRoomWebSocketController {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/chatroom/create")
    public void createChatRoom(ChatRoomDTOrequest request, Principal principal) throws Exception {
        ChatRoomEntity chatRoom = chatRoomService.create(request.getRoomName(), principal.getName());

        messagingTemplate.convertAndSend("/topic/chats", chatRoom);
    }

    @MessageMapping("/chatroom/get")
    public void getChatRooms(Principal principal) throws Exception {
        List<ChatRoomEntity> chatRooms = chatRoomService.getAll(); // измените этот метод в соответствии с вашей логикой

        messagingTemplate.convertAndSendToUser(principal.getName(), "/topic/getchats", chatRooms);
    }



    /*@MessageMapping("/chatroom/add/user")
    @SendTo("/topic/chatroom")
    public UserEntity addUserInChatRoom(){
    }*/
}

