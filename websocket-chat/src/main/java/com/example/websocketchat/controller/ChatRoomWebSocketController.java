package com.example.websocketchat.controller;

import com.example.websocketchat.entity.ChatRoomEntity;
import com.example.websocketchat.entity.UserEntity;
import com.example.websocketchat.model.DTO.ChatRoomDTOrequest;
import com.example.websocketchat.model.DTO.AddUserDTOrequest;
import com.example.websocketchat.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
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
        messagingTemplate.convertAndSendToUser(principal.getName(), "/topic/chats", chatRoom);
    }

    @MessageMapping("/chatroom/get")
    public void getChatRooms(Principal principal) throws Exception {
        Set<ChatRoomEntity> chatRooms = chatRoomService.getAllForUser(principal.getName());
        messagingTemplate.convertAndSendToUser(principal.getName(), "/topic/chats/get", chatRooms);
    }

    @MessageMapping("/chatroom/user/add")
    public void addUserInChatRoom(AddUserDTOrequest request, Principal principal){
        try {

            if (request.getUsername() != null && request.getUsername().equals(principal.getName())) {
                throw new Exception("Error");
            }

            UserEntity user = chatRoomService.getUser(request.getUsername());
            ChatRoomEntity chatRoom = chatRoomService.getChatRoom(request.getChatId());

            if (chatRoomService.userHere(user.getId(), chatRoom.getId())){
                throw new Exception("Error");
            }

            chatRoomService.addUserInChatRoom(user, chatRoom);
            messagingTemplate.convertAndSendToUser(request.getUsername(),"/topic/chats", chatRoom);
        } catch (Exception err){
            messagingTemplate.convertAndSendToUser(principal.getName(),"/topic/error/add/user", "{\"message\": \"Error!\"}");
        }
    }
}

