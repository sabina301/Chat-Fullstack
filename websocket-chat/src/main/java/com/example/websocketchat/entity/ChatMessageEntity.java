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
    private String content;
    //private MessageType type;
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private ChatRoomEntity chatRoom;

}
