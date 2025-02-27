package com.patrol.domain.chat.chatMessage.entity;

import com.amazonaws.services.kms.model.MessageType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ChatMessage  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private MessageType type;
    private String content;
    private Long sender;
    private Long receiver;
    private Long roomId;
    private Long postId;
    private LocalDateTime timestamp;

    public enum MessageType {
      CHAT,TYPING
    }
}
