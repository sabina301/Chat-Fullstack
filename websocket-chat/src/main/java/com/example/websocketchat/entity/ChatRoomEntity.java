package com.example.websocketchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ChatMessageEntity> messages = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "chatRooms", cascade = CascadeType.REFRESH)
    @JsonIgnore
    private Set<UserEntity> users = new HashSet<>();

    public void addUser(UserEntity user){
        users.add(user);
    }

    public void deleteUser(UserEntity user){
        users.remove(user);
    }

    public void addMessage(ChatMessageEntity message){
        messages.add(message);
    }

    public ChatRoomEntity(String name){
        this.name = name;
    }
}
