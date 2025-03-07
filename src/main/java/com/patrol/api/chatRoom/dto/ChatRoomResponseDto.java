package com.patrol.api.chatRoom.dto;

import com.patrol.api.chatMessage.dto.ResponseMessage;
import com.patrol.api.lostFoundPost.dto.LostFoundPostResponseDto;
import com.patrol.api.member.member.dto.MemberResponseDto;
import com.patrol.domain.chatRoom.entity.ChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomResponseDto {
    private Long id;
    private String roomIdentifier;
    private MemberResponseDto member1;
    private MemberResponseDto member2;
    private LostFoundPostResponseDto post;
    private ResponseMessage lastMessage; // 가장 최근 메시지만 포함
    private int unreadCount; // 읽지 않은 메시지 수

    public ChatRoomResponseDto(ChatRoom chatRoom, ResponseMessage lastMessage, int unreadCount) {
        this.id = chatRoom.getId();
        this.roomIdentifier = chatRoom.getRoomIdentifier();
        this.member1 = new MemberResponseDto(chatRoom.getMember1());
        this.member2 = new MemberResponseDto(chatRoom.getMember2());
        this.post = new LostFoundPostResponseDto(chatRoom.getPost());
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }
}
