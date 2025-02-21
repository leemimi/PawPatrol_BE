package com.patrol.domain.findPost.repository;
import com.patrol.domain.findPost.entity.FindPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FindPostRepository extends JpaRepository<FindPost, Long> {
    // LostPost의 lostId에 해당하는 FindPost들을 Pageable을 통해 조회
    Page<FindPost> findByLostPost_Id(Long lostPostId, Pageable pageable);  // ✅ 올바른 코드
    Page<FindPost> findByLostPostIsNull(Pageable pageable); // 독립적인 제보글만 조회
    Page<FindPost> findByLostPostIsNotNull(Pageable pageable); // 신고글과 연계된 제보글 조회

}
