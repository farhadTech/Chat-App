package com.example.chatapp.service;

import com.example.chatapp.entities.Message;
import com.example.chatapp.entities.Room;

import java.util.List;

public interface RoomService {
    List<Room> getAllRooms();
    Room createRoom(String roomId);
    Room joinRoom(String roomId);
    Room updateRoom(String roomId, Room room);
    List<Message> readMessages(String roomId, int page, int size);
}
