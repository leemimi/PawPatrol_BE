package com.patrol.domain.chat.chatRoom.entity;

import com.patrol.domain.chat.chatMessage.entity.ChatMessage;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ChatRoom extends BaseEntity {

    private Long user1;

    private Long user2;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages;

    private Long postId;

    public ChatRoom(Long user1, Long user2, Long postId) {
        this.user1= user1;
        this.user2= user2;
        this.postId= postId;
        this.messages = new ArrayList<>();
    }

    public void addMessage(ChatMessage message) {
        this.messages.add(message);
    }

}
