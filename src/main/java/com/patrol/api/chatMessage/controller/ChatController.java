package com.patrol.api.chatMessage.controller;

import com.patrol.api.chatMessage.dto.RequestMessage;
import com.patrol.domain.chatMessage.service.ChatMessageService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.rsData.RsData;
import com.patrol.global.webMvc.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageService chatMessageService;

    @GetMapping("/rooms/{identifier}/messages")
    @ResponseBody
    public RsData<Object> getChatMessages(@PathVariable("identifier") String identifier,
                                          @LoginUser Member loginUser) {
        return chatMessageService.getChatMessages(identifier, loginUser);
    }

    @PostMapping("/rooms/{identifier}/read")
    @ResponseBody
    public RsData<Object> markMessagesAsRead(@PathVariable("identifier") String identifier,
                                             @LoginUser Member loginUser) {
        return chatMessageService.markMessagesAsRead(identifier, loginUser);
    }
}
