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
public class SendImgDTOresponse {
    private String senderName;
    private MessageType type;
    @Lob
    private byte[] byteImg;
}
