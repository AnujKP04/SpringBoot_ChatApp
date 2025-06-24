package com.chatapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;



@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig  implements WebSocketMessageBrokerConfigurer{
	
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		System.out.println("Register endpoint is working");
		registry.addEndpoint("/ws")
		  .addInterceptors(new UserHandshakeInterceptor())
		  .setHandshakeHandler(new CustomUserHandshakeHandler())
		  .setAllowedOrigins("http://localhost:5173")
		  .withSockJS();

	}
	
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app");
		registry.enableSimpleBroker("/topic","/queue");
		registry.setUserDestinationPrefix("/user");
	}
	
	

}
