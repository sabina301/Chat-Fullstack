package com.example.websocketchat.model.DTO;

import com.example.websocketchat.model.MessageType;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendTextDTOresponse {
    private String messageContent;
    private String chatId;
    private MessageType type;
    private String senderName;
}
