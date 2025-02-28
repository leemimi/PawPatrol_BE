package com.patrol.domain.chatMessage.service;

import com.patrol.api.chatMessage.dto.RequestMessage;
import com.patrol.api.chatMessage.dto.ResponseMessage;
import com.patrol.api.member.member.dto.MemberResponseDto;
import com.patrol.domain.chatMessage.entity.ChatMessage;
import com.patrol.domain.chatMessage.repository.ChatMessageRepository;
import com.patrol.domain.chatRoom.entity.ChatRoom;
import com.patrol.domain.chatRoom.repository.ChatRoomRepository;
import com.patrol.domain.lostFoundPost.entity.LostFoundPost;
import com.patrol.domain.lostFoundPost.repository.LostFoundPostRepository;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.repository.MemberRepository;
import com.patrol.global.rsData.RsData;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final LostFoundPostRepository lostFoundPostRepository;
    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageService.class);

    @Transactional
    public RsData<Object> writeMessage(Long postId, RequestMessage requestMessage) {
        try {
            // 1. 빈 메시지 또는 250자 초과 메시지 검사
            if (requestMessage.getContent() == null || requestMessage.getContent().trim().isEmpty()) {
                return new RsData<>("400", "채팅 메시지는 비어 있을 수 없습니다.");
            }
            if (requestMessage.getContent().length() > 250) {
                return new RsData<>("400", "채팅 메시지는 250자를 넘을 수 없습니다.");
            }

            Optional<LostFoundPost> postOptional = lostFoundPostRepository.findById(postId);
            if (postOptional.isEmpty()) {
                return new RsData<>("404", "해당 게시물을 찾을 수 없습니다.");
            }
            LostFoundPost post = postOptional.get();

            Member receiver;
            Member sender;

            if (requestMessage.getReceiverId() != null) {
                Optional<Member> receiverOptional = memberRepository.findById(requestMessage.getReceiverId());
                Optional<Member> senderOptional = memberRepository.findById(requestMessage.getSenderId());
                if (receiverOptional.isEmpty()) {
                    return new RsData<>("404", "수신자를 찾을 수 없습니다.");
                }
                sender=senderOptional.get();
                receiver = receiverOptional.get();
            } else {
                return new RsData<>("404", "수신자를 찾을 수 없습니다.");
            }

            ChatRoom chatRoom;
            Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByPostAndMembers(post, sender, receiver);

            if (chatRoomOptional.isPresent()) {
                chatRoom = chatRoomOptional.get();
            } else {
                String roomIdentifier = ChatRoom.createRoomIdentifier(post, sender, receiver);
                chatRoom = ChatRoom.builder()
                        .post(post)
                        .member1(sender)
                        .member2(receiver)
                        .roomIdentifier(roomIdentifier)
                        .build();

                chatRoomRepository.save(chatRoom);
                logger.info("New chat room created: {}", roomIdentifier);
            }

            ChatMessage chatMessage = ChatMessage.builder()
                    .content(requestMessage.getContent())
                    .sender(sender)
                    .receiver(receiver)
                    .chatRoom(chatRoom)
                    .isRead(false)
                    .build();
            chatMessageRepository.save(chatMessage);

            ResponseMessage messageDTO = new ResponseMessage(
                    chatMessage.getId(),
                    chatMessage.getContent(),
                    new MemberResponseDto(sender),
                    new MemberResponseDto(receiver),
                    post.getId(),
                    chatMessage.getCreatedAt(),
                    chatMessage.isRead()
            );

            // 메시지를 채팅방으로 전송
            simpMessagingTemplate.convertAndSend("/queue/chat/" + chatRoom.getRoomIdentifier(),
                    Map.of(
                            "type", "MESSAGE",
                            "data", messageDTO
                    ));

            return new RsData<>("200", "채팅 메시지 작성 성공", chatRoom.getId());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument: ", e);
            return new RsData<>("403", e.getMessage());
        } catch (Exception e) {
            logger.error("Error sending message: ", e);
            return new RsData<>("500", "서버 내부 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 특정 채팅방의 메시지를 모두 조회
     */
    public RsData<Object> getChatMessages(String identifier, Member loginUser) {
        try {
            Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByRoomIdentifier(identifier);
            if (chatRoomOptional.isEmpty()) {
                return new RsData<>("404", "채팅방을 찾을 수 없습니다.");
            }

            ChatRoom chatRoom = chatRoomOptional.get();

            if (!chatRoom.getMember1().getId().equals(loginUser.getId()) &&
                    !chatRoom.getMember2().getId().equals(loginUser.getId())) {
                return new RsData<>("403", "해당 채팅방에 접근 권한이 없습니다.");
            }

            List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoom(chatRoom);
            List<ResponseMessage> responseDtos = chatMessages.stream()
                    .map(chatMessage -> new ResponseMessage(
                            chatMessage.getId(),
                            chatMessage.getContent(),
                            new MemberResponseDto(chatMessage.getSender()),
                            new MemberResponseDto(chatMessage.getReceiver()),
                            chatMessage.getChatRoom().getPost().getId(),
                            chatMessage.getCreatedAt(),
                            chatMessage.isRead()
                    ))
                    .collect(Collectors.toList());

            return new RsData<>("200", "채팅 메시지 조회 성공", responseDtos);
        } catch (Exception e) {
            logger.error("Error retrieving messages: ", e);
            return new RsData<>("500", "서버 내부 오류가 발생했습니다.");
        }
    }

    /**
     * 메시지 읽음 처리
     */
    @Transactional
    public RsData<Object> markMessagesAsRead(String identifier, Member loginUser) {
        try {
            Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByRoomIdentifier(identifier);
            if (chatRoomOptional.isEmpty()) {
                return new RsData<>("404", "채팅방을 찾을 수 없습니다.");
            }

            ChatRoom chatRoom = chatRoomOptional.get();

            // 해당 채팅방의 참여자인지 확인
            if (!chatRoom.getMember1().getId().equals(loginUser.getId()) &&
                    !chatRoom.getMember2().getId().equals(loginUser.getId())) {
                return new RsData<>("403", "해당 채팅방에 접근 권한이 없습니다.");
            }

            List<ChatMessage> unreadMessages = chatMessageRepository.findAllByChatRoomAndReceiverAndIsReadFalse(
                    chatRoom, loginUser);

            for (ChatMessage message : unreadMessages) {
                message.setRead(true);
            }

            return new RsData<>("200", "메시지 읽음 처리 완료");
        } catch (Exception e) {
            logger.error("Error marking messages as read: ", e);
            return new RsData<>("500", "서버 내부 오류가 발생했습니다.");
        }
    }
}
