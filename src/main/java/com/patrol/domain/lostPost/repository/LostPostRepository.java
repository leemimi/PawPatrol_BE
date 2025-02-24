package com.patrol.domain.lostPost.repository;
import com.patrol.domain.lostPost.entity.LostPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LostPostRepository extends JpaRepository<LostPost, Long> {
}
