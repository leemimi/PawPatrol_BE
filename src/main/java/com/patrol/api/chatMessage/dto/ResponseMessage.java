package com.patrol.api.chatMessage.dto;

import com.patrol.api.member.member.dto.MemberDto;
import com.patrol.api.member.member.dto.MemberResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
}
