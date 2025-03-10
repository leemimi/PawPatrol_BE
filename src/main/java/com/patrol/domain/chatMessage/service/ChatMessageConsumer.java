package com.patrol.domain.chatMessage.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.patrol.api.chatMessage.dto.ResponseMessage;
import com.patrol.domain.notification.service.FCMNotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageConsumer {
    private final SimpMessagingTemplate messagingTemplate;
    private final Logger logger = LoggerFactory.getLogger(ChatMessageConsumer.class);
    private final FCMNotificationService fcmNotificationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "real-time-chat-messages", groupId = "chat-group")
    public void consumeRealTimeChatMessage(String messageJson) {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            ResponseMessage message = objectMapper.readValue(messageJson, ResponseMessage.class);
            messagingTemplate.convertAndSend(
                    "/queue/chat/" + message.getRoomIdentifier() ,
                    message
            );
        } catch (Exception e) {
            logger.error("Error processing real-time chat message: ", e);
        }
    }

    @KafkaListener(topics = "offline-notifications", groupId = "notification-group")
    public void consumeRealTimeChatAlarm(String messageJson) {
        try {

            objectMapper.registerModule(new JavaTimeModule());
            ResponseMessage message = objectMapper.readValue(messageJson, ResponseMessage.class);

            fcmNotificationService.sendChatNotification(message, message.getReceiver().getEmail());
        } catch (Exception e) {
            logger.error("Error processing notification: ", e);
        }
    }
}