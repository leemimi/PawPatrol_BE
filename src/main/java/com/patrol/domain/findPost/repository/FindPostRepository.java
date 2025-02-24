package com.patrol.domain.findPost.repository;
import com.patrol.domain.findPost.entity.FindPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FindPostRepository extends JpaRepository<FindPost, Long> {
    // LostPost의 lostId에 해당하는 FindPost들을 Pageable을 통해 조회
    Page<FindPost> findByLostPost_Id(Long lostId, Pageable pageable);
    // JPQL 예시
//    @Query("SELECT fp FROM FindPost fp LEFT JOIN FETCH fp.lostPost WHERE fp.foundId = :foundId")
//    FindPost findPostWithLostPost(@Param("foundId") Long foundId);

    @Query(value = "SELECT f FROM FindPost f " +
            "WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(f.latitude)) * " +
            "cos(radians(f.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
            "sin(radians(f.latitude)))) <= :radius/1000")
    List<FindPost> findPostsWithinRadius(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radius
    );

}
