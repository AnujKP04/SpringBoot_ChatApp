package com.chatapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatapp.model.ChatMessage;
import com.chatapp.repository.ChatMessageRepository;

@Service
public class ChatMessageService {

	@Autowired
	ChatMessageRepository repository;
	
	public List<ChatMessage> getChatHistory(String sender, String receiver){
		return repository.findChatHistory(sender, receiver);
	}
}
