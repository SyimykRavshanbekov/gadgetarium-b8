package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.model.SubProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubProductRepository extends JpaRepository<SubProduct, Long> {
  @Query("SELECT u.lastViews from User u where u.id = ?1")
  List<SubProduct>getAllLastReviews(Long userId);
}