package com.patrol.domain.comment.entity;
/*
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.patrol.domain.LostPost.entity.LostPost;
import com.patrol.domain.LostFound.entity.FindPost;
import com.patrol.domain.member.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Comment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @JsonBackReference
    @ManyToOne
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lost_id")
    private LostPost lostPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "found_id")
    private FindPost lostFound;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();
}

*/
