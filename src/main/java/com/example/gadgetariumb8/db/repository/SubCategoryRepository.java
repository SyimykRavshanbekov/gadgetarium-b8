package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.dto.response.SubCategoryResponse;
import com.example.gadgetariumb8.db.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    List<SubCategoryResponse> findAllByCategory_Id(Long categoryId);
}