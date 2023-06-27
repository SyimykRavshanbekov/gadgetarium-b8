package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r.images from Review r where r.id=?1")
    List<String> getAllImage(Long id);

    @Modifying
    @Query(nativeQuery = true, value = "delete from reviews r where r.product_id = ?1")
    void deleteByProductId(Long productId);

    @Modifying
    @Query(nativeQuery = true, value = "delete from review_images ri where ri.review_id = ?1")
    void deleteReviewImgByReviewId(Long id);
}