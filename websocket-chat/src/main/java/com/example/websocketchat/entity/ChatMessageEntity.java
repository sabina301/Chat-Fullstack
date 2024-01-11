package com.example.websocketchat.entity;

import com.example.websocketchat.model.MessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String senderName;
    private String messageContent;
    private MessageType type;

    @Lob
    private byte[] byteImg;
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private ChatRoomEntity chatRoom;

    public ChatMessageEntity(Long id, String senderName, String messageContent, MessageType type, LocalDateTime timestamp) {
        this.id = id;
        this.senderName = senderName;
        this.messageContent = messageContent;
        this.type = type;
        this.timestamp = timestamp;
    }

    public ChatMessageEntity(Long id, String senderName, MessageType type, byte[] byteImg,LocalDateTime timestamp) {
        this.id = id;
        this.senderName = senderName;
        this.type = type;
        this.byteImg = byteImg;
        this.timestamp = timestamp;
    }


}
