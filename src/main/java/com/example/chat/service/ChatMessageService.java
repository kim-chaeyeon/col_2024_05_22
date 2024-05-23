package com.example.chat.service;


import com.example.chat.model.ChatMessage;
import com.example.chat.repo.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository repository;

    public ChatMessage save(ChatMessage message) {
        return repository.save(message);
    }


}

