package com.example.websocketchat.service;

import com.example.websocketchat.entity.ChatRoomEntity;
import com.example.websocketchat.entity.UserEntity;
import com.example.websocketchat.repository.ChatRoomRepository;
import com.example.websocketchat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Set<ChatRoomEntity> getAllForUser(String username) throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(()->{
            return new Exception("Dont have user with this name");
        });
        return user.getChatRooms();
    }

    public UserEntity getUser(String username) throws Exception {
        return userRepository.findByUsername(username).orElseThrow(()->{
            return new Exception("Dont have user with this name");
        });
    }

    public ChatRoomEntity getChatRoom(String chatId) throws Exception {
        return chatRoomRepository.findById(Long.valueOf(chatId)).orElseThrow(()->{
            return new Exception("Dont have user with this name");
        });
    }

    public void addUserInChatRoom(UserEntity user, ChatRoomEntity chatRoom){
        chatRoom.addUser(user);
        user.addChatRoom(chatRoom);
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
