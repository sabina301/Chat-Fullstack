package com.example.websocketchat.service;

import com.example.websocketchat.entity.ChatRoomEntity;
import com.example.websocketchat.entity.UserEntity;
import com.example.websocketchat.repository.ChatRoomRepository;
import com.example.websocketchat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    public ChatRoomEntity create(String roomName, String username) throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(()->{
            return new Exception("Dont have user with this name");
        });
        ChatRoomEntity chatRoom = new ChatRoomEntity(roomName);

        chatRoomRepository.save(chatRoom);
        chatRoom.addUser(user);
        user.addChatRoom(chatRoom);
        return chatRoom;
    }

    public List<ChatRoomEntity> getAll(){
        return chatRoomRepository.findAll();
    }

    /*public Long findChatRoom(Long senderId, Long recipientId){
        ChatRoomEntity chatRoom = chatRoomRepository.findChatRoomBySenderAndRecipient(senderId, recipientId)
                .or(()->{
                    createChatRoom(senderId, recipientId);
                    return;
                });
        return chatRoom.getId();
    }

    public void createChatRoom(Long senderId, Long recipientId){

        ChatRoomEntity senderChatRoom = ChatRoomEntity
                .builder()
                .recipientId(recipientId)
                .build();

        ChatRoomEntity recipientChatRoom = ChatRoomEntity
                .builder()
                .recipientId(senderId)
                .build();

        chatRoomRepository.save(senderChatRoom);
        chatRoomRepository.save(recipientChatRoom);
    }*/
}
