package com.example.chat.controller;

import com.example.chat.model.ChatRoom;
import com.example.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/rooms")
    public String rooms(Model model) {
        return "room";  // Assuming 'room.html' is under the 'src/main/resources/templates' directory
    }

    @GetMapping("/api/rooms")
    @ResponseBody
    public ResponseEntity<List<ChatRoom>> getAllRooms() {
        List<ChatRoom> rooms = chatRoomRepository.findAll();
        return ResponseEntity.ok(rooms);
    }


    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity<ChatRoom> createRoom(@RequestParam("name") String name) {
        ChatRoom newRoom = new ChatRoom();
        newRoom.setName(name);
        ChatRoom savedRoom = chatRoomRepository.save(newRoom);
        return ResponseEntity.ok(savedRoom);
    }

    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable("roomId") UUID roomId) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(roomId);
        chatRoom.ifPresent(room -> model.addAttribute("roomId", room.getRoomId()));
        return chatRoom.isPresent() ? "roomdetail" : "redirect:/chat/rooms";
    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<?> roomInfo(@PathVariable("roomId") UUID roomId) {
        try {
            ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow(() -> new Exception("Room not found"));
            return ResponseEntity.ok(room);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving room: " + e.getMessage());
        }
    }
}