package com.patrol.api.chatMessage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessage {
    private Long receiverId;
    private Long senderId;
    private String content;
}
