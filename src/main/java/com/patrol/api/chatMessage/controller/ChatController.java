package com.patrol.api.chatMessage.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.patrol.api.chatMessage.dto.RequestMessage;
import com.patrol.api.lostFoundPost.dto.LostFoundPostRequestDto;
import com.patrol.api.lostFoundPost.dto.LostFoundPostResponseDto;
import com.patrol.domain.chatMessage.service.ChatMessageService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.rsData.RsData;
import com.patrol.global.webMvc.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "채팅 기능", description = "실시간 1:1 채팅")
public class ChatController {
    private final ChatMessageService chatMessageService;

    @GetMapping("/rooms/{identifier}/messages")
    @ResponseBody
    @Operation(summary = "채팅 메시지 히스토리 조회")
    public RsData<Object> getChatMessages(@PathVariable("identifier") String identifier,
                                          @LoginUser Member loginUser) {
        return chatMessageService.getChatMessages(identifier, loginUser);
    }

    @PostMapping("/rooms/{identifier}/read")
    @ResponseBody
    @Operation(summary = "메시지 읽음 표시")
    public RsData<Object> markMessagesAsRead(@PathVariable("identifier") String identifier,
                                             @LoginUser Member loginUser) {
        return chatMessageService.markMessagesAsRead(identifier, loginUser);
    }

    @PostMapping("/images/{postId}")
    @Operation(summary = "채팅 이미지 등록")
    public RsData<Long> sendImagethroughChat(
            @RequestParam(value = "images") List<MultipartFile> images,
            @RequestParam(value = "roomIdentifier") String roomIdentifier,
            @LoginUser Member loginUser) {
            return chatMessageService.createImage(roomIdentifier, images,  loginUser);
    }
}
