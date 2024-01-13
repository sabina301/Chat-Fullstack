package com.example.websocketchat.repository;

import com.example.websocketchat.entity.ChatMessageEntity;
import com.example.websocketchat.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findByMessageContentContainingAndChatRoom(String message, ChatRoomEntity chatRoom);
}
