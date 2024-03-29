package com.example.websocketchat.model.DTO;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SendMessageDTOrequest {
    private String message;
    private String chatId;
    private String type;
}
