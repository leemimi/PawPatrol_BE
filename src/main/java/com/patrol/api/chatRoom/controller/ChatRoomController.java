package com.patrol.api.chatRoom.controller;

import com.patrol.domain.chatMessage.service.ChatMessageService;
import com.patrol.domain.chatRoom.service.ChatRoomService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.rsData.RsData;
import com.patrol.global.webMvc.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chatlist")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    @ResponseBody
    public RsData<Object> getChatRooms(@LoginUser Member loginUser) {
        return chatRoomService.getUserChatRooms(loginUser);
    }

}
