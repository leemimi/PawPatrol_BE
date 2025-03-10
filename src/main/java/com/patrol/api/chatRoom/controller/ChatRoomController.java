package com.patrol.api.chatRoom.controller;

import com.patrol.domain.chatRoom.entity.ChatRoomType;
import com.patrol.domain.chatRoom.service.ChatRoomService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.rsData.RsData;
import com.patrol.global.webMvc.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chatlist")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "채팅방 조회 기능", description = "채팅방 목록 조회 기능")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    @ResponseBody
    @Operation(summary = "채팅방 목록 조회")
    public RsData<Object> getChatRooms(
            @RequestParam(required = false) ChatRoomType type,
            @LoginUser Member loginUser) {
        if (type != null) {
            return chatRoomService.getUserChatRoomsByType(loginUser, type);
        }
        return chatRoomService.getUserChatRooms(loginUser);
    }

}
