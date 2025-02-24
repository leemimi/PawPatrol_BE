package com.patrol.domain.lostPost.repository;
import com.patrol.domain.lostPost.entity.LostPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostPostRepository extends JpaRepository<LostPost, Long> {

    @Query(value = "SELECT l FROM LostPost l " +
            "WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(l.latitude)) * " +
            "cos(radians(l.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
            "sin(radians(l.latitude)))) <= :radius/1000")
    List<LostPost> lostPostsWithinRadius(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radius
    );
}
