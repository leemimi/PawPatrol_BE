package com.patrol.api.notification.dto;

import com.patrol.domain.notification.entity.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

//알림 저장인데 현재 안쓰임.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private Long id;
    private String content;
    private NotificationType type;
    private Long senderId;
    private String senderUsername;
    private boolean isRead;
    private Long relatedPostId;
    private Long relatedChatRoomId;
    private LocalDateTime createdAt;
}