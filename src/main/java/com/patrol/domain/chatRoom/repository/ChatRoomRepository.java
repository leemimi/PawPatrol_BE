package com.patrol.domain.chatRoom.repository;

import com.patrol.domain.chatRoom.entity.ChatRoom;
import com.patrol.domain.lostFoundPost.entity.LostFoundPost;
import com.patrol.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.post = :post " +
            "AND ((cr.member1 = :member1 AND cr.member2 = :member2) OR (cr.member1 = :member2 AND cr.member2 = :member1))")
    Optional<ChatRoom> findByPostAndMembers(@Param("post") LostFoundPost post,
                                            @Param("member1") Member member1,
                                            @Param("member2") Member member2);

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.member1 = :member OR cr.member2 = :member ORDER BY cr.modifiedAt DESC")
    List<ChatRoom> findAllByMember(@Param("member") Member member);

    Optional<ChatRoom> findByRoomIdentifier(String identifier);

    @Query("SELECT DISTINCT c FROM ChatRoom c " +
            "LEFT JOIN FETCH c.member1 m1 " +
            "LEFT JOIN FETCH c.member2 m2 " +
            "LEFT JOIN FETCH c.post p " +
            "WHERE m1 = :member OR m2 = :member")
    List<ChatRoom> findAllWithDetailsByMember(@Param("member") Member member);


}
