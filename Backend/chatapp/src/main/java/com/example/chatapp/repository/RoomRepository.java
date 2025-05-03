package com.example.chatapp.repository;

import com.example.chatapp.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    @Query("""
            SELECT r FROM Room r WHERE r.roomId = :roomId
            """)
    Room findByRoomId(String roomId);
}
