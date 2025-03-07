package com.patrol.api.chatMessage.dto;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.patrol.api.member.member.dto.MemberResponseDto;
import com.patrol.domain.chatMessage.entity.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {
    private Long id;
    private String content;
    private MemberResponseDto sender;
    private MemberResponseDto receiver;
    private Long postId;
    private LocalDateTime timestamp;
    private boolean isRead;
    private MessageType messageType;
    private String roomIdentifier;

}
