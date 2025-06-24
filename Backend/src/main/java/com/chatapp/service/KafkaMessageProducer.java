package com.chatapp.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageProducer {
	
	private final KafkaTemplate<String, String> kafkaTemplate;
	
	public KafkaMessageProducer(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	
	public void sendMessage(String topic, String message) {
		 System.out.println("Sending message to Kafka topic: " + topic);
		kafkaTemplate.send(topic,message);
		
	}

}
