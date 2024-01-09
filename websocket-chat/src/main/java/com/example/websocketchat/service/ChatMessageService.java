package com.example.websocketchat.service;

import com.example.websocketchat.entity.ChatMessageEntity;
import com.example.websocketchat.entity.ChatRoomEntity;
import com.example.websocketchat.entity.UserEntity;
import com.example.websocketchat.exception.ChatroomNotFoundException;
import com.example.websocketchat.exception.UserNotFoundException;
import com.example.websocketchat.model.MessageType;
import com.example.websocketchat.repository.ChatMessageRepository;
import com.example.websocketchat.repository.ChatRoomRepository;
import com.example.websocketchat.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public void sendMessage(String messageContent, String chatRoomId, String username){
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User is not found"));
        ChatRoomEntity chatRoom = chatRoomRepository.findById(Long.valueOf(chatRoomId)).orElseThrow(()->new ChatroomNotFoundException("Chatroom is not found"));
        ChatMessageEntity message = new ChatMessageEntity();
        message.setMessageContent(messageContent);
        message.setChatRoom(chatRoom);
        message.setSenderName(user.getUsername());
        message.setType(MessageType.CHAT);
        message.setTimestamp(LocalDateTime.now());
        chatMessageRepository.save(message);

        chatRoom.addMessage(message);
        chatRoomRepository.save(chatRoom);
    }
    public List<ChatMessageEntity> getMessages(String chatId){
        ChatRoomEntity chatRoom = chatRoomRepository.findById(Long.valueOf(chatId)).orElseThrow(()->new ChatroomNotFoundException("Chatroom is not found"));
        List<ChatMessageEntity> messages = entityManager.createQuery("FROM ChatMessageEntity WHERE chatRoom = :chatRoom ORDER BY timestamp", ChatMessageEntity.class)
                .setParameter("chatRoom", chatRoom)
                .getResultList();

        return messages;
    }


}
