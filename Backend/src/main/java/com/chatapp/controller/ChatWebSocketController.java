package com.chatapp.controller;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.model.ChatMessage;
import com.chatapp.service.ChatMessageService;
import com.chatapp.service.KafkaMessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin("http://localhost:5173")
public class ChatWebSocketController {
	
	@Autowired
	private ChatMessageService chatService;

	private KafkaMessageProducer producer;
	
	public ChatWebSocketController(KafkaMessageProducer producer)
	{
		this.producer= producer;
	}
	
	@MessageMapping("/chat")
	public void handlerChat(ChatMessage message) throws Exception{
		System.out.println("Controller is call");
	    System.out.println("Message received from frontend: " + message.getContent());
		message.setTimestamp(new Timestamp(System.currentTimeMillis()));
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(message);
		
		producer.sendMessage("chat-topic", json);
	}
	
	@GetMapping("/message")
	public List<ChatMessage> getChatHistory(@RequestParam String sender, @RequestParam String receiver){
		return chatService.getChatHistory(sender, receiver);
	}

}
