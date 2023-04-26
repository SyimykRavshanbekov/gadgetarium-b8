package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.dto.response.ReviewResponse;
import com.example.gadgetariumb8.db.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select new com.example.gadgetariumb8.db.dto.response.ReviewResponse(r.id,r.product.PDF," +
            "s.itemNumber,r.commentary,r.grade,r.answer,concat(r.user.firstName,' ',r.user.lastName) ," +
            "r.user.userInfo.email,r.user.image) from Review r join r.product.subProducts s")
    List<ReviewResponse> findAllResponse();

    @Query("select r.images from Review r where r.id=?1")
    List<String> getAllImage(Long id);

}