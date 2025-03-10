package com.patrol.api.chatMessage.controller;

import com.patrol.api.chatMessage.dto.RequestMessage;
import com.patrol.domain.chatMessage.service.ChatMessageService;
import com.patrol.domain.chatRoom.entity.ChatRoomType;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.webMvc.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatMessageMappingController {
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/{postId}")
    @Operation(summary = "실시간 메시지 전송")
    public void sendMessage(@DestinationVariable("postId") Long postId,
                            @Payload RequestMessage requestMessage) {
        ChatRoomType type = requestMessage.getType();

        chatMessageService.writeMessage(postId, requestMessage, type);
    }


}
