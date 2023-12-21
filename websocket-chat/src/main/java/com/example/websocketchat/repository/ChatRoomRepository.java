package com.example.websocketchat.repository;

import com.example.websocketchat.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    /*Optional<ChatRoomEntity> findChatRoomBySenderAndRecipient(Long senderId, Long recipientId);*/
}
