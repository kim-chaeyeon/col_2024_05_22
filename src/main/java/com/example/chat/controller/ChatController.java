package com.example.chat.controller;

import com.example.chat.model.ChatMessage;
import com.example.chat.repository.ChatMessageRepository;
import com.example.chat.service.ChatMessageService;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ChatController {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatMessageService messageService;

    @MessageMapping("/chat/message/{roomId}")
    @SendTo("/sub/chat/room/{roomId}")
    public ChatMessage sendMessage(@Payload ChatMessage message, @DestinationVariable("roomId") String roomId) {
        try {
            if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
                message.setMessage(message.getSender() + "님이 입장하셨습니다.");
            }
            message.setRoomId(roomId);
            return messageService.save(message);
        } catch (Exception e) {

            return null;
        }
    }
//    @GetMapping("/chat/messages/{roomId}")
//    public ResponseEntity<List<Message>> getMessages(@PathVariable("roomId") String roomId) {
//        List<Message> messages = chatMessageRepository.findByRoomIdOrderByTimestampDesc(roomId);
//        return ResponseEntity.ok(messages);
//    }

    @MessageMapping("/chat/private/{roomId}")
    @SendTo("/sub/chat/private/{roomId}")
    public ChatMessage sendPrivateMessage(@Payload ChatMessage message, @DestinationVariable("roomId") String roomId) {
        message.setRoomId(roomId);
        return messageService.save(message);
    }

}