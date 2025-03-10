package com.patrol.domain.notification.entity;

import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.chatMessage.entity.ChatMessage;
import com.patrol.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "notifications")
public class Notification extends BaseEntity {


    @Column(nullable = false)
    private String body;

    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;


    @Column(name = "related_chat_room_id")
    private Long relatedChatRoomId;


}
