package com.example.websocketchat.entity;

import com.example.websocketchat.model.MessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

}
