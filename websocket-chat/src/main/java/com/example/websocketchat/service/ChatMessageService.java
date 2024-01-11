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
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void sendTextMessage(String messageContent, String chatRoomId, String username){
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User is not found"));
        ChatRoomEntity chatRoom = chatRoomRepository.findById(Long.valueOf(chatRoomId)).orElseThrow(()->new ChatroomNotFoundException("Chatroom is not found"));
        ChatMessageEntity message = new ChatMessageEntity();
        message.setMessageContent(messageContent);
        message.setChatRoom(chatRoom);
        message.setSenderName(user.getUsername());
        message.setType(MessageType.TEXT);
        message.setByteImg(new byte[]{0});
        message.setTimestamp(LocalDateTime.now());
        chatMessageRepository.save(message);

        chatRoom.addMessage(message);
        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void sendImgMessage(byte[] byteImg, String chatRoomId, String username){
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User is not found"));
        ChatRoomEntity chatRoom = chatRoomRepository.findById(Long.valueOf(chatRoomId)).orElseThrow(()->new ChatroomNotFoundException("Chatroom is not found"));

        ChatMessageEntity message = new ChatMessageEntity();
        message.setMessageContent("Image");
        message.setChatRoom(chatRoom);
        message.setSenderName(user.getUsername());
        message.setType(MessageType.IMG);
        message.setByteImg(byteImg);
        message.setTimestamp(LocalDateTime.now());

        chatMessageRepository.save(message);

        chatRoom.addMessage(message);
        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public List<ChatMessageEntity> getMessages(String chatId){
        ChatRoomEntity chatRoom = chatRoomRepository.findById(Long.valueOf(chatId)).orElseThrow(()->new ChatroomNotFoundException("Chatroom is not found"));
        List<ChatMessageEntity> messages = entityManager.createQuery("SELECT id, senderName, messageContent, type, timestamp FROM ChatMessageEntity WHERE chatRoom = :chatRoom ORDER BY timestamp", ChatMessageEntity.class)
                .setParameter("chatRoom", chatRoom)
                .getResultList();
        for (ChatMessageEntity message : messages) {
            byte[] byteImg = chatMessageRepository.findById(message.getId()).get().getByteImg();
            message.setByteImg(byteImg);
        }
        return messages;
    }
}
