package com.example.websocketchat.service;

import com.example.websocketchat.entity.ChatRoomEntity;
import com.example.websocketchat.entity.UserEntity;
import com.example.websocketchat.exception.ChatroomNotFoundException;
import com.example.websocketchat.exception.UserNotFoundException;
import com.example.websocketchat.model.DTO.GetMessageDTOrequest;
import com.example.websocketchat.model.UserStatus;
import com.example.websocketchat.repository.ChatRoomRepository;
import com.example.websocketchat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ChatRoomRepository chatRoomRepository;

    public List<UserEntity> findConnectedUsers(){
        return userRepository.findAllByStatus(UserStatus.ONLINE);
    }

    public List<UserEntity> findAllUsers(){
        return userRepository.findAll();
    }
}
