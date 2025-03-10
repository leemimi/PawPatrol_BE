package com.patrol.api.chatRoom.dto;

import com.patrol.api.animalCase.dto.AnimalCaseDetailDto;
import com.patrol.api.chatMessage.dto.ResponseMessage;
import com.patrol.api.lostFoundPost.dto.LostFoundPostResponseDto;
import com.patrol.api.member.member.dto.MemberResponseDto;
import com.patrol.domain.chatRoom.entity.ChatRoom;
import com.patrol.domain.chatRoom.entity.ChatRoomType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomResponseDto {
    private Long id;
    private String roomIdentifier;
    private MemberResponseDto member1;
    private MemberResponseDto member2;
    private Object post; // Object 타입으로 선언하여 두 가지 타입의 DTO를 담을 수 있음
    private ChatRoomType type; // 채팅방 타입 추가
    private ResponseMessage lastMessage;
    private int unreadCount;

    public ChatRoomResponseDto(ChatRoom chatRoom, ResponseMessage lastMessage, int unreadCount) {
        this.id = chatRoom.getId();
        this.roomIdentifier = chatRoom.getRoomIdentifier();
        this.member1 = new MemberResponseDto(chatRoom.getMember1());
        this.member2 = new MemberResponseDto(chatRoom.getMember2());
        this.type = chatRoom.getType();

        // 타입에 따라 적절한 DTO 생성
        if (chatRoom.getType() == ChatRoomType.LOSTFOUND) {
            this.post = new LostFoundPostResponseDto(chatRoom.getLostFoundPost());
        } else {
            this.post = AnimalCaseDetailDto.of(chatRoom.getAnimalCase());
        }

        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }
}