package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}