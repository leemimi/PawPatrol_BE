package com.patrol.domain.image.repository;

import com.patrol.domain.image.entity.Image;
import com.patrol.domain.lostFoundPost.entity.LostFoundPost;
import com.patrol.domain.lostFoundPost.entity.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findById(Long Id);
    List<Image> findAllByFoundId(Long postId);
    List<Image> findAllByAnimalId(Long animalId);

    void delete(Image image);

    Image findByAnimalId(Long petId);

    List<Image> findAllByAnimalIdIsNotNull ();

    List<Image> findByEmbeddingIsNull ();

    List<Image> findByFoundIdIsNotNullAndEmbeddingIsNotNull ();

    List<Image> findAllByAnimalIdIsNotNullAndEmbeddingIsNotNull ();

    Image findByPath (String path);

    @Query("SELECT i FROM Image i WHERE i.embedding IS NOT NULL OR i.features IS NOT NULL")
    List<Image> findByEmbeddingIsNotNullOrFeaturesIsNotNull();

    @Query("""
    SELECT i FROM Image i 
    WHERE (i.embedding IS NOT NULL OR i.features IS NOT NULL) AND (
        i.foundId IN (
            SELECT p.id FROM LostFoundPost p 
            WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(p.latitude)) 
            * cos(radians(p.longitude) - radians(:lng)) 
            + sin(radians(:lat)) * sin(radians(p.latitude)))) <= :radius
        ) 
        OR i.animalId IS NOT NULL
    )
    """)
    List<Image> findNearbyImagesAndRegisteredAnimals(
            @Param("lat") double latitude,
            @Param("lng") double longitude,
            @Param("radius") double radius);



    List<Image> findByStatus (PostStatus postStatus);

    @Query("SELECT i FROM Image i WHERE i.id = :imageId ORDER BY i.createdAt DESC LIMIT 1")
    Optional<Image> findLatestById(@Param("imageId") Long imageId);

    @Query("SELECT i FROM Image i WHERE i.id = :imageId AND i.status IS NOT NULL")
    Optional<Image> findByIdAndStatusIsNotNull(@Param("imageId") Long imageId);

    Image findByFoundId (Long foundId);
}
