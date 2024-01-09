package com.example.websocketchat.model.DTO;

import com.example.websocketchat.model.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageStatusDTOrequest {
    private String status;
    private String username;

    public MessageStatusDTOrequest(MessageType messageType, String username) {
        this.status = messageType.name();
        this.username = username;
    }
}
