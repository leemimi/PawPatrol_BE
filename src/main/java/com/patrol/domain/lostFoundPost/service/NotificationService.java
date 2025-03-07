package com.patrol.domain.lostFoundPost.service;

import com.patrol.api.lostFoundPost.dto.LostFoundPostResponseDto;
import com.patrol.domain.lostFoundPost.entity.LostFoundPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // LostFoundPost 등록 후 알림 전송
    public void sendLostFoundPostNotification(LostFoundPost lostFoundPost) {
        LostFoundPostResponseDto responseDto = LostFoundPostResponseDto.from(lostFoundPost);

        // 클라이언트로 알림을 보내는 부분. /topic/lost-found-posts 에 메시지 전송
        messagingTemplate.convertAndSend("/topic/lost-found-posts", responseDto);
    }
}
