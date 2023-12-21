package com.example.websocketchat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.security.Principal;
import java.util.Collections;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /*@Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("REG HANDLER");
        registry.addHandler(myHandler(), "/ws")
                .setAllowedOrigins("*")
                .withSockJS();
    }
    @Bean
    public WebSocketHandler myHandler() {
        System.out.println("!!! MY HANDLER !!!");
        return new TextWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(org.springframework.web.socket.WebSocketSession session) throws Exception {
                // Получение аутентификационных данных из SecurityContextHolder
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("!!!!!!!AUTH!!!! " + authentication.isAuthenticated() + " " + authentication.getName());
                Principal principal = () -> authentication.getName();

                // Установка аутентификации для текущей WebSocket-сессии
                session.getAttributes().put("user", principal);
            }

            @Override
            public void afterConnectionClosed(org.springframework.web.socket.WebSocketSession session, CloseStatus status) throws Exception {
                // Очистка аутентификации после закрытия WebSocket-сессии
                System.out.println("AFTER");
                session.getAttributes().remove("user");
            }
        };
    }*/
}
