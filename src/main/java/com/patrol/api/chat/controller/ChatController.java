package com.patrol.api.chat.controller;

import com.patrol.domain.chat.chatMessage.entity.ChatMessage;
import com.patrol.domain.chat.chatRoom.entity.ChatRoom;
import com.patrol.domain.chat.service.ChatService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.webMvc.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/users")
    public ResponseEntity<List<Member>> getAllUsers() {
        List<Member> users = chatService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<Member> getUser(@LoginUser Member loginUser) {
        Member user = chatService.getUser(loginUser.getId());
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/users/{username}/rooms")
    public ResponseEntity<List<ChatRoom>> getUserChatRooms(@LoginUser Member loginUser) {
        List<ChatRoom> rooms = chatService.getUserChatRooms(loginUser.getId());
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/rooms")
    public ResponseEntity<ChatRoom> createOrGetChatRoom(@LoginUser Member loginUser,
                                                        @RequestParam Long user2,
                                                        @RequestParam Long postId) {
        ChatRoom room = chatService.getChatRoom(loginUser.getId(), user2, postId);
        return ResponseEntity.ok(room);
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ChatRoom> getChatRoom(@PathVariable String roomId,
                                                @LoginUser Member loginUser) {
        ChatRoom room = chatService.getChatRoomById(roomId);
        if (room != null) {
            return ResponseEntity.ok(room);
        }
        return ResponseEntity.notFound().build();
    }

    // 채팅방 메시지 목록 조회
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<ChatMessage>> getChatRoomMessages(@PathVariable String roomId,
                                                                 @LoginUser Member loginUser) {
        ChatRoom room = chatService.getChatRoomById(roomId);
        if (room != null) {
            return ResponseEntity.ok(room.getMessages());
        }
        return ResponseEntity.notFound().build();
    }
}
