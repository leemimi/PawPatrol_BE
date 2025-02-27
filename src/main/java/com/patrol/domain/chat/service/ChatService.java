package com.patrol.domain.chat.service;

import com.patrol.domain.chat.chatMessage.entity.ChatMessage;
import com.patrol.domain.chat.chatRoom.entity.ChatRoom;
import com.patrol.domain.chat.chatRoom.repository.ChatRoomRepository;
import com.patrol.domain.member.member.entity.Member;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class ChatService {
    private ChatRoomRepository chatRoomRepository;

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private Map<String, Member> users = new HashMap<>();

    private Map<String, ChatRoom> chatRooms = new HashMap<>();

    private Map<String, String> userSessions = new HashMap<>();

    public void addUser(Member user) {
        users.put(user.getNickname(), user);
    }

    public void saveUserSession(Long userId, String sessionId) {
        userSessions.put(String.valueOf(userId), sessionId);
    }

    public String getUserSessionId(Long userId) {
        return userSessions.get(userId);
    }

    public List<Member> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public Member getUser(Long userId) {
        return users.get(userId);
    }

    public ChatRoom getChatRoom(Long user1, Long user2, Long postId) {
        String roomId = createRoomId(user1, user2);

        if (!chatRooms.containsKey(roomId)) {
            ChatRoom chatRoom = new ChatRoom( user1, user2, postId);
            chatRooms.put(roomId, chatRoom);
            logger.info("New chat room created: {}", roomId);
        }

        return chatRooms.get(roomId);
    }

    private String createRoomId(Long user1, Long user2) {
        return user1.compareTo(user2) < 0
                ? user1 + "_" + user2
                : user2 + "_" + user1;
    }

    public ChatRoom getChatRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public void saveChatMessage(ChatMessage message) {
        if (chatRooms.containsKey(message.getRoomId())) {
            chatRooms.get(message.getRoomId()).addMessage(message);
            logger.info("Message saved in room {}: {}", message.getRoomId(), message.getContent());
        }
    }

    public List<ChatRoom> getUserChatRooms(Long userId) {
        return chatRoomRepository.findByUser1OrUser2(userId, userId);
    }
}
