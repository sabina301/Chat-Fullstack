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
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ChatMessageEntity> messages = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "chatroom_user",
        joinColumns = @JoinColumn(name = "chatroom_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"))
    private Set<UserEntity> users = new HashSet<>();

    public void addUser(UserEntity user){
        users.add(user);
    }

    public ChatRoomEntity(String name){
        this.name = name;
    }
}
