package com.patrol.domain.lostFoundPost.repository;
import com.patrol.domain.lostFoundPost.entity.LostFoundPost;
import com.patrol.domain.lostFoundPost.entity.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostFoundPostRepository extends JpaRepository<LostFoundPost, Long> {

    @Query(value = "SELECT f FROM LostFoundPost f " +
            "WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(f.latitude)) * " +
            "cos(radians(f.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
            "sin(radians(f.latitude)))) <= :radius/1000")
    List<LostFoundPost> findPostsWithinRadius(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radius
    );

    Page<LostFoundPost> findByStatus(PostStatus status, Pageable pageable);
    LostFoundPost findByPetId(Long petId);

}
