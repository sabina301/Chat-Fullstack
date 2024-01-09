package com.example.websocketchat.service;

import com.example.websocketchat.entity.ChatRoomEntity;
import com.example.websocketchat.entity.UserEntity;
import com.example.websocketchat.exception.UserNotFoundException;
import com.example.websocketchat.model.DTO.UserGetDTOresponse;
import com.example.websocketchat.repository.ChatRoomRepository;
import com.example.websocketchat.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;


    public ChatRoomEntity create(String roomName, String username){
        UserEntity user = userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException("Don't have user with this name"));
        ChatRoomEntity chatRoom = new ChatRoomEntity(roomName);

        chatRoomRepository.save(chatRoom);
        chatRoom.addUser(user);
        user.addChatRoom(chatRoom);
        userRepository.save(user);
        return chatRoom;
    }

    public Set<ChatRoomEntity> getAllForUser(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException("Don't have user with this name"));
        return user.getChatRooms();
    }

    public UserEntity getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException("Don't have user with this name"));
    }

    public ChatRoomEntity getChatRoom(String chatId) {
        return chatRoomRepository.findById(Long.valueOf(chatId)).orElseThrow(()-> new UserNotFoundException("Don't have user with this name"));
    }

    @Transactional
    public void addUserInChatRoom(UserEntity user, ChatRoomEntity chatRoom){
        UserEntity existingUser = userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        ChatRoomEntity existingChatRoom = chatRoomRepository.findById(chatRoom.getId()).orElseThrow(() -> new EntityNotFoundException("Chat room not found"));

        existingChatRoom.addUser(existingUser);
        existingUser.addChatRoom(existingChatRoom);
    }

    public Boolean userHere(Long userId, Long chatId){
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        ChatRoomEntity chatRoom = chatRoomRepository.findById(chatId).orElseThrow(()->new EntityNotFoundException("ChatRoom not found"));

        Set<UserEntity> users = chatRoom.getUsers();

        for (UserEntity userEntity: users){
            if (userEntity.getUsername().equals(user.getUsername())){
                return true;
            }
        }
        return false;
    }

    public Set<UserGetDTOresponse> getUsers(String chatId){
        ChatRoomEntity chatRoomEntity = getChatRoom(chatId);
        Set<UserEntity> userEntities = chatRoomEntity.getUsers();
        Set<UserGetDTOresponse> users = new HashSet<>();
        for (UserEntity userEntity: userEntities) {
            UserGetDTOresponse userGetDTOresponse = new UserGetDTOresponse(userEntity.getId(), userEntity.getUsername());
            users.add(userGetDTOresponse);
        }
        return users;
    }
}
