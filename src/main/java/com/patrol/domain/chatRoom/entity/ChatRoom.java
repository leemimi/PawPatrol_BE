package com.patrol.domain.chatRoom.entity;

import com.patrol.domain.chatMessage.entity.ChatMessage;
import com.patrol.domain.lostFoundPost.entity.LostFoundPost;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@SuperBuilder
public class ChatRoom extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private LostFoundPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member1_id")
    private Member member1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member2_id")
    private Member member2;

    private String roomIdentifier;

    public static String createRoomIdentifier(LostFoundPost post, Member member1, Member member2) {
        Long postId = post.getId();
        Long smallerId = Math.min(member1.getId(), member2.getId());
        Long largerId = Math.max(member1.getId(), member2.getId());

        return String.format("%d_%d_%d", postId, smallerId, largerId);
    }
}
