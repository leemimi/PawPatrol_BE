package com.patrol.domain.comment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.patrol.domain.lostFoundPost.entity.LostFoundPost;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {


    @Column(name = "content")
    private String content;

    @JsonIgnore
    @ManyToOne
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lostfound_id")
    private LostFoundPost lostFoundPost;  // ✅ 올바른 변수명

}
