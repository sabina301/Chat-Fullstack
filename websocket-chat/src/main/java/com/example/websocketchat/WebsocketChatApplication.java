package com.example.websocketchat;

import com.example.websocketchat.model.Role;
import com.example.websocketchat.repository.RoleRepository;
import com.example.websocketchat.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;


@SpringBootApplication
@EnableWebSocketMessageBroker
public class WebsocketChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketChatApplication.class, args);
	}
	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncode){
		return args ->{
			roleRepository.save(new Role("USER"));
		};
	}
}
