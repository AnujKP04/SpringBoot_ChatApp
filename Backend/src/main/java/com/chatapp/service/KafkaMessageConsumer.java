package com.chatapp.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.chatapp.model.ChatMessage;
import com.chatapp.repository.ChatMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaMessageConsumer {
	
	private final ChatMessageRepository repository;
	private final SimpMessagingTemplate messagingTemplate;
	
	public KafkaMessageConsumer(ChatMessageRepository repository, SimpMessagingTemplate messagingTemplate) {
		this.repository = repository;
		this.messagingTemplate = messagingTemplate;
	}
	
	@KafkaListener(topics= "chat-topic", groupId= "chat-group")
	public void consume(String json) throws Exception {
		 System.out.println("Kafka received: " + json);  
		ObjectMapper mapper = new ObjectMapper();
		ChatMessage message = mapper.readValue(json, ChatMessage.class);
		
		System.out.println("====="+message.getReceiver());
		
		messagingTemplate.convertAndSendToUser(
			    message.getReceiver(),     
			    "/queue/messages",        
			    message                   
			);
		repository.save(message);
		
		
	}

}
