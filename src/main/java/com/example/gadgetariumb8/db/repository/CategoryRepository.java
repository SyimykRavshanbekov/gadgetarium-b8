package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}