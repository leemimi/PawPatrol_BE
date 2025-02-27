package com.patrol.domain.chat.chatRoom.repository;

import com.patrol.domain.chat.chatRoom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByUser1OrUser2(Long user1, Long user2);
}
