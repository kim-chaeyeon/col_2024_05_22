package com.example.chat.chatting.repository;

import com.example.chat.chatting.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
//    List<Message> findByRoomIdOrderByTimestampDesc(String roomId);
}
