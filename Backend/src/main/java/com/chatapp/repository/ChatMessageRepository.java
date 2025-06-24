package com.chatapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chatapp.model.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
	
	@Query("select m from ChatMessage m where m.sender= :sender and m.receiver= :receiver or "
			+ " m.sender= :receiver and m.receiver= :sender order by m.timestamp ASC")
	List<ChatMessage> findChatHistory(@Param("sender") String sender,@Param("receiver") String receiver);

}
