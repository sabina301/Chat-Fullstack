package com.example.websocketchat.model.DTO;

import com.example.websocketchat.model.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageDTOresponse {
    private String messageContent;
    private String senderName;
    private MessageType type;
}
