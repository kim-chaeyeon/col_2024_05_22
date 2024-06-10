package com.example.chat.repository;

import com.example.chat.model.ChatMessage;
import org.apache.logging.log4j.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
//    List<Message> findByRoomIdOrderByTimestampDesc(String roomId);
}
