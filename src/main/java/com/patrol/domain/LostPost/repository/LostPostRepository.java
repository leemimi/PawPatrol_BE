package com.patrol.domain.LostPost.repository;
import com.patrol.domain.LostPost.entity.LostPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LostPostRepository extends JpaRepository<LostPost, Long> {
}
