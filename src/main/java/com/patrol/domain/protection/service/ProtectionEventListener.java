package com.patrol.domain.protection.service;

import com.patrol.api.chatMessage.dto.RequestMessage;
import com.patrol.domain.animalCase.entity.AnimalCase;
import com.patrol.domain.animalCase.enums.CaseHistoryStatus;
import com.patrol.domain.animalCase.events.ProtectionStatusChangeEvent;
import com.patrol.domain.animalCase.repository.AnimalCaseRepository;
import com.patrol.domain.chatMessage.service.ChatMessageService;
import com.patrol.domain.chatRoom.entity.ChatRoomType;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.protection.entity.Protection;
import com.patrol.domain.protection.repository.ProtectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProtectionEventListener {

    private final ChatMessageService chatMessageService;
    private final ProtectionRepository protectionRepository;
    private final AnimalCaseRepository animalCaseRepository;

    @EventListener
    @Transactional
    public void handleProtectionApplication(ProtectionStatusChangeEvent event) {
        System.out.println("보호 신청 이벤트 발생");
        if (event.getHistoryStatus() == CaseHistoryStatus.TEMP_PROTECT_REQUEST) {

            Protection protection = protectionRepository.findById(event.getProtectionId())
                    .orElseThrow(() -> new RuntimeException("보호 신청을 찾을 수 없습니다."));

            AnimalCase animalCase = protection.getAnimalCase();
            Member applicant = protection.getApplicant();
            Member currentFoster = animalCase.getCurrentFoster();

            AnimalCase tempPost = animalCaseRepository.findById(animalCase.getId())
                    .orElseThrow(() -> new RuntimeException("관련 임보글을 찾을 수 없습니다."));

            Long postId = tempPost.getId();

            String initialMessage = String.format(
                    "안녕하세요! %s님께서 %s에 대한 %s 신청을 하셨습니다.\n\n" +
                            "신청 이유: %s\n" +
                            "신청 유형: %s\n\n" +
                            "추가 문의사항은 이 채팅을 통해 연락주세요.",
                    applicant.getNickname(),
                    animalCase.getTitle(),
                    protection.getProtectionType(),
                    protection.getReason(),
                    protection.getProtectionType()
            );

            RequestMessage requestMessage = new RequestMessage();
            requestMessage.setContent(initialMessage);
            requestMessage.setSenderId(applicant.getId());
            requestMessage.setReceiverId(currentFoster.getId());
            System.out.println("보호 신청 메시지 전송");
            chatMessageService.writeMessage(postId, requestMessage, ChatRoomType.PROTECTADOPT);
        }
    }
}
