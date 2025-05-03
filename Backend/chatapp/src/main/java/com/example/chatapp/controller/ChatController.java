package com.example.chatapp.controller;

import com.example.chatapp.entities.Message;
import com.example.chatapp.entities.Room;
import com.example.chatapp.payload.MessageRequest;
import com.example.chatapp.repository.RoomRepository;
import com.example.chatapp.service.RoomService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@CrossOrigin("http://localhost:3000")
public class ChatController {
    private final RoomRepository roomRepository;
    private RoomService roomService;

    public ChatController(RoomService roomService, RoomRepository roomRepository) {
        this.roomService = roomService;
        this.roomRepository = roomRepository;
    }


    public Message sendMessage(
            @RequestBody MessageRequest request,
            @DestinationVariable String roomId
    ) {
        Room room = roomRepository.findByRoomId(request.getRoomId());

        Message message = new Message();
        message.setContent(request.getContent());
        message.setSender(request.getSender());
        message.setTimeStamp(LocalDateTime.now());

        if(room != null) {
            room.getMessages().add(message);
            roomRepository.save(room);
        } else {
            throw new RuntimeException("Room not found");
        }
        return message;
    }
}




