package com.example.websocketchat.model.DTO;

import com.example.websocketchat.model.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageStatusDTOrequest {
    private String type;
    private String senderName;

    public MessageStatusDTOrequest(MessageType messageType, String username) {
        this.type = messageType.name();
        this.senderName = username;
    }
}
