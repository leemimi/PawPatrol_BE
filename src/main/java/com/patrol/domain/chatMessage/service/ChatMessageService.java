package com.patrol.domain.chatMessage.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrol.api.chatMessage.dto.RequestMessage;
import com.patrol.api.chatMessage.dto.ResponseMessage;
import com.patrol.api.image.dto.ImageResponseDto;
import com.patrol.api.member.member.dto.MemberResponseDto;
import com.patrol.domain.animal.repository.AnimalRepository;
import com.patrol.domain.chatMessage.entity.ChatMessage;
import com.patrol.domain.chatMessage.entity.MessageType;
import com.patrol.domain.chatMessage.repository.ChatMessageRepository;
import com.patrol.domain.chatRoom.entity.ChatRoom;
import com.patrol.domain.chatRoom.repository.ChatRoomRepository;
import com.patrol.domain.image.entity.Image;
import com.patrol.domain.image.repository.ImageRepository;
import com.patrol.domain.lostFoundPost.entity.LostFoundPost;
import com.patrol.domain.lostFoundPost.repository.LostFoundPostRepository;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.repository.MemberRepository;
import com.patrol.global.error.ErrorCode;
import com.patrol.global.exception.CustomException;
import com.patrol.global.rsData.RsData;
import com.patrol.global.storage.FileStorageHandler;
import com.patrol.global.storage.FileUploadRequest;
import com.patrol.global.storage.FileUploadResult;
import com.patrol.global.storage.NcpObjectStorageService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final FileStorageHandler fileStorageHandler;
    private final ObjectMapper objectMapper;
    private final NcpObjectStorageService ncpObjectStorageService;

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
                sender = senderOptional.get();
                receiver = receiverOptional.get();
            } else {
                return new RsData<>("404", "수신자를 찾을 수 없습니다.");
            }

            ChatRoom chatRoom;
            Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByPostAndMembers(post, sender, receiver);
            boolean isNewChatRoom = false;

            if (chatRoomOptional.isPresent()) {
                chatRoom = chatRoomOptional.get();
            } else {
                isNewChatRoom = true;
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

            ResponseMessage messageDTO = ResponseMessage.builder()
                    .id(chatMessage.getId())
                    .content(chatMessage.getContent())
                    .sender(new MemberResponseDto(sender))
                    .receiver(new MemberResponseDto(receiver))
                    .postId(post.getId())
                    .timestamp(chatMessage.getCreatedAt())
                    .isRead(chatMessage.isRead())
                    .messageType(chatMessage.getMessageType()) // Make sure to set this field
                    .build();

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

    @Transactional
    public RsData<Long> createImage(String roomIdentifier, List<MultipartFile> images, Member loginUser) {
        try {
            if (images == null || images.isEmpty()) {
                return new RsData<>("400", "이미지가 필요합니다.", null);
            }
            ChatRoom chatRoom;
            Member receiver;
            LostFoundPost post;

            Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByRoomIdentifier(roomIdentifier);

            if (chatRoomOptional.isPresent()) {
                chatRoom = chatRoomOptional.get();
                if (chatRoom.getMember1().getId().equals(loginUser.getId())) {
                    receiver = chatRoom.getMember2();
                } else {
                    receiver = chatRoom.getMember1();
                }
                post = chatRoom.getPost();
            } else {
                String[] parts = roomIdentifier.split("_");
                if (parts.length != 3) {
                    return new RsData<>("400", "잘못된 채팅방 식별자 형식입니다.", null);
                }

                Long postId = Long.parseLong(parts[0]);
                Long memberId1 = Long.parseLong(parts[1]);
                Long memberId2 = Long.parseLong(parts[2]);

                Long receiverId = (loginUser.getId().equals(memberId1)) ? memberId2 : memberId1;

                Optional<LostFoundPost> postOptional = lostFoundPostRepository.findById(postId);
                if (postOptional.isEmpty()) {
                    return new RsData<>("404", "해당 게시물을 찾을 수 없습니다.", null);
                }
                post = postOptional.get();

                Optional<Member> receiverOptional = memberRepository.findById(receiverId);
                if (receiverOptional.isEmpty()) {
                    return new RsData<>("404", "수신자를 찾을 수 없습니다.", null);
                }
                receiver = receiverOptional.get();

                chatRoom = ChatRoom.builder()
                        .post(post)
                        .member1(loginUser)
                        .member2(receiver)
                        .roomIdentifier(roomIdentifier)
                        .build();

                chatRoomRepository.save(chatRoom);
            }

            // 이미지 업로드 및 메시지 처리
            List<String> uploadedPaths = new ArrayList<>();
            List<ImageResponseDto> imageDtos = new ArrayList<>();

            try {
                for (MultipartFile image : images) {
                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentLength(image.getSize());
                    metadata.setContentType(image.getContentType());

                    // 이미지 업로드
                    FileUploadResult uploadResult = fileStorageHandler.handleFileUpload(
                            FileUploadRequest.builder()
                                    .folderPath("chat/images/")
                                    .file(image)
                                    .build()
                    );

                    if (uploadResult != null) {
                        String fullPath = uploadResult.getFullPath();
                        uploadedPaths.add(fullPath);

                        ImageResponseDto imageDto = new ImageResponseDto(
                                fullPath
                        );
                        imageDtos.add(imageDto);
                    }
                }

                // 이미지 정보를 JSON으로 변환
                String imageContent = objectMapper.writeValueAsString(imageDtos);

                // 채팅 메시지 생성
                ChatMessage chatMessage = ChatMessage.builder()
                        .content(imageContent)
                        .messageType(MessageType.IMAGE)
                        .sender(loginUser)
                        .receiver(receiver)
                        .chatRoom(chatRoom)
                        .isRead(false)
                        .build();

                chatMessageRepository.save(chatMessage);

                // 응답 메시지 생성
                ResponseMessage messageDTO = ResponseMessage.builder()
                        .id(chatMessage.getId())
                        .content(imageContent)
                        .messageType(MessageType.IMAGE)
                        .sender(new MemberResponseDto(loginUser))
                        .receiver(new MemberResponseDto(receiver))
                        .postId(post.getId())
                        .timestamp(chatMessage.getCreatedAt())
                        .isRead(chatMessage.isRead())
                        .build();

                simpMessagingTemplate.convertAndSend(
                        "/queue/chat/" + chatRoom.getRoomIdentifier(),
                        Map.of("type", "MESSAGE", "data", messageDTO)
                );

                return new RsData<>("200", "이미지 메시지 전송 성공", chatRoom.getId());

            } catch (Exception e) {
                for (String path : uploadedPaths) {
                    try {
                        ncpObjectStorageService.delete(path);
                    } catch (Exception deleteError) {
                        logger.error("Failed to delete image after error: {}", deleteError.getMessage());
                    }
                }
                logger.error("Error in creating image message: {}", e.getMessage(), e);
                throw new CustomException(ErrorCode.DATABASE_ERROR);
            }
        } catch (Exception e) {
            logger.error("Unexpected error in createImageMessage: {}", e.getMessage(), e);
            return new RsData<>("500", "서버 오류가 발생했습니다.", null);
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
                    .map(chatMessage -> ResponseMessage.builder()
                            .id(chatMessage.getId())
                            .content(chatMessage.getContent())
                            .sender(new MemberResponseDto(chatMessage.getSender()))
                            .receiver(new MemberResponseDto(chatMessage.getReceiver()))
                            .postId(chatMessage.getChatRoom().getPost().getId())
                            .timestamp(chatMessage.getCreatedAt())
                            .isRead(chatMessage.isRead())
                            .messageType(chatMessage.getMessageType()) // ChatMessage에 이 필드가 없다면 이 줄을 조정하세요
                            .build())
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
