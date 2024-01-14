package com.example.websocketchat.controller;

import com.example.websocketchat.entity.ChatRoomEntity;
import com.example.websocketchat.entity.UserEntity;
import com.example.websocketchat.model.DTO.*;
import com.example.websocketchat.model.MessageType;
import com.example.websocketchat.service.ChatRoomService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chatroom/create")
    public void createChatRoom(ChatRoomDTOrequest request, Principal principal)  {
        ChatRoomEntity chatRoom = chatRoomService.create(request.getRoomName(), principal.getName());
        messagingTemplate.convertAndSendToUser(principal.getName(), "/topic/chatroom/create", chatRoom);
    }

    @MessageMapping("/chatroom/get/one")
    public void getChatRoom(ChatRoomSelectDTOrequest request, Principal principal)  {
        ChatRoomEntity chatRoom = chatRoomService.getChatRoom(request.getChatId());
        messagingTemplate.convertAndSendToUser(principal.getName(),"/topic/chatroom/get/one", chatRoom);
    }

    @Transactional
    @MessageMapping("/chatroom/get/all")
    public void getChatRooms(Principal principal){
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

            if (chatRoomService.userHereById(user.getId(), chatRoom.getId())){
                throw new Exception("Error");
            }

            chatRoomService.addUserInChatRoom(user, chatRoom);
            messagingTemplate.convertAndSendToUser(request.getUsername(),"/topic/chatroom/create", chatRoom);
            chatRoomService.saveMessageStatus(MessageType.JOIN, request.getUsername(), request.getChatId());
            messagingTemplate.convertAndSend("/topic/messages/status/" + request.getChatId(), new MessageStatusDTOrequest(MessageType.JOIN, request.getUsername()));
        } catch (Exception err){
            messagingTemplate.convertAndSendToUser(principal.getName(),"/topic/error/add/user", "{\"message\": \"Error!\"}");
        }
    }

    @MessageMapping("/chatroom/info/get")
    public void getChatroomInfo(ChatRoomSelectDTOrequest request, Principal principal){
        ChatRoomEntity chatRoom = chatRoomService.getChatRoom(request.getChatId());
        messagingTemplate.convertAndSendToUser(principal.getName(),"/topic/chatroom/info/get",chatRoom);
        Set<UserGetDTOresponse> userEntities = chatRoomService.getUsers(request.getChatId());
        messagingTemplate.convertAndSendToUser(principal.getName(),"/topic/chatroom/info/get/users", userEntities);
        int[] quantityOfRes = chatRoomService.getQuantityOfRes(request.getChatId());
        messagingTemplate.convertAndSendToUser(principal.getName(),"/topic/chatroom/info/get/res",quantityOfRes);
    }

    @MessageMapping("/chatroom/user/exit")
    public String exitUser(@RequestBody GetMessageDTOrequest request, Principal principal){
        chatRoomService.exitUser(principal.getName(), request.getChatId());
        chatRoomService.saveMessageStatus(MessageType.LEAVE, principal.getName(), request.getChatId());
        messagingTemplate.convertAndSend("/topic/messages/status/" + request.getChatId(),  new MessageStatusDTOrequest(MessageType.LEAVE, principal.getName()));
        return "chat.html";
    }

}

