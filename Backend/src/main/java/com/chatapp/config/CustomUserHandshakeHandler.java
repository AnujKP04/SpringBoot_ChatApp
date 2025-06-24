package com.chatapp.config;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;

import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class CustomUserHandshakeHandler  extends DefaultHandshakeHandler{
	
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String,Object> attributes) {
		
		String username= (String) attributes.get("user");
		 System.out.println("extracted username = " + username);

		
		if (username == null) {
            System.out.println("Username is null in determineUser");
        }
		
		return ()-> username;
	}

}
