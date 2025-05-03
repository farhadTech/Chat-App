package com.example.chatapp.service;

import com.example.chatapp.entities.*;

import com.example.chatapp.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService{
    private RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room createRoom(String roomId) {
        if (roomRepository.findByRoomId(roomId) != null) {
            throw new RuntimeException("Room with id " + roomId + " already exists");
        }
        Room room = new Room();
        room.setRoomId(roomId);
        Room savedRoom = roomRepository.save(room);
        return savedRoom;
    }

    @Override
    public Room joinRoom(String roomId) {
        Room room = roomRepository.findByRoomId(roomId);
        if (room == null) {
            throw new RuntimeException("Room with id " + roomId + " does not exist");
        }
        return room;
    }

    @Override
    public Room updateRoom(String roomId, Room room) {
       Room existingRoom = roomRepository.findByRoomId(roomId);
        if (existingRoom == null) {
            throw new RuntimeException("Room with id " + roomId + " does not exist");
        }
        return roomRepository.save(room);
    }


    @Override
    public List<Message> readMessages(String roomId, int page, int size) {
        Room room = roomRepository.findByRoomId(roomId);
        if(room == null) {
            throw new RuntimeException("Room with id " + roomId + " does not exist");
        }
        List<Message> messages = room.getMessages();

        int start = Math.max(0, messages.size() - (page + 1) * size);
        int end = Math.min(messages.size(), start + size);

        return messages.subList(start, end);
    }
}
