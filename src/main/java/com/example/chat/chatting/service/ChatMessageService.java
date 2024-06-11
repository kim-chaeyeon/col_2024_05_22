package com.example.chat.chatting.service;


import com.example.chat.chatting.model.ChatMessage;
import com.example.chat.chatting.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessagerepository;

    public ChatMessage save(ChatMessage message) {
        return chatMessagerepository.save(message);
    }


}

