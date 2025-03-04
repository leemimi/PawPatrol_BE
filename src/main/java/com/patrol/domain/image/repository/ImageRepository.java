package com.patrol.domain.image.repository;

import com.patrol.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findById(Long Id);
    List<Image> findAllByFoundId(Long postId);

    void delete(Image image);

    Image findByAnimalId(Long petId);

    List<Image> findAllByAnimalIdIsNotNull ();

    List<Image> findByEmbeddingIsNull ();

    List<Image> findByFoundIdIsNotNullAndEmbeddingIsNotNull ();

    List<Image> findAllByAnimalIdIsNotNullAndEmbeddingIsNotNull ();

    Image findByPath (String path);
}
