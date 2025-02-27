package com.patrol.api.chat.websocket.controller;

import com.patrol.api.chat.controller.ChatController;
import com.patrol.domain.chat.chatMessage.entity.ChatMessage;
import com.patrol.domain.chat.chatRoom.entity.ChatRoom;
import com.patrol.domain.chat.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatWebSocketController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        logger.info("Received message: {}", chatMessage.getContent());

        chatMessage.setTimestamp(LocalDateTime.now());

        if (chatMessage.getRoomId() == null) {
            ChatRoom room = chatService.getChatRoom(chatMessage.getSender(), chatMessage.getReceiver(), chatMessage.getPostId());
            chatMessage.setRoomId(room.getId());
        }
        chatService.saveChatMessage(chatMessage);

        String receiverSession = chatService.getUserSessionId(chatMessage.getReceiver());
        if (receiverSession != null) {
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(chatMessage.getReceiver()),
                    "/queue/messages",
                    chatMessage
            );
        }
    }

    @MessageMapping("/chat.join")
    public void joinChat(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        Long senderId = chatMessage.getSender();
        Long targetId = chatMessage.getReceiver();
        Long postId = chatMessage.getPostId();

        headerAccessor.getSessionAttributes().put("userId", senderId);

        chatService.saveUserSession(senderId, headerAccessor.getSessionId());

        ChatRoom room = chatService.getChatRoom(senderId, targetId, postId);
        chatMessage.setRoomId(room.getId());
        chatMessage.setTimestamp(LocalDateTime.now());

    }

    @MessageMapping("/chat.typing")
    public void typingIndicator(@Payload ChatMessage chatMessage) {
        chatMessage.setType(ChatMessage.MessageType.TYPING);

        messagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getReceiver()),
                "/queue/typing",
                chatMessage
        );
    }
}
