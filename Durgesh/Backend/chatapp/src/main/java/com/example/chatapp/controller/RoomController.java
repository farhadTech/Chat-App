package com.example.chatapp.controller;

import com.example.chatapp.entities.Message;
import com.example.chatapp.entities.Room;
import com.example.chatapp.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@CrossOrigin("http://localhost:3000")
public class RoomController {
    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody String roomId) {
        Room savedRoom = roomService.createRoom(roomId);
        return new ResponseEntity<>(savedRoom, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> AllRoom = roomService.getAllRooms();
        return new ResponseEntity<>(AllRoom, HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Room> joinRoom(@PathVariable String roomId) {
        Room room = roomService.joinRoom(roomId);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<Room> updateRoom(@PathVariable String roomId, @RequestBody Room room) {
        Room updatedRoom = roomService.updateRoom(roomId, room);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    // get messages of the room
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessages(
            @PathVariable String roomId,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size
    ) {
        // get messages
        List<Message> messages = roomService.readMessages(roomId, page, size);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}

















