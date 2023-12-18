package com.example.websocketchat.service;

import com.example.websocketchat.entity.UserEntity;
import com.example.websocketchat.model.UserStatus;
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

    public void addUser(UserEntity user){
        user.setStatus(UserStatus.ONLINE);
        userRepository.save(user);
    }

    public void disconnectUser(UserEntity user){
        user.setStatus(UserStatus.OFFLINE);
        userRepository.save(user);
    }

    public List<UserEntity> findConnectedUsers(){
        return userRepository.findAllByStatus(UserStatus.ONLINE);
    }

    public List<UserEntity> findAllUsers(){
        return userRepository.findAll();
    }
}
