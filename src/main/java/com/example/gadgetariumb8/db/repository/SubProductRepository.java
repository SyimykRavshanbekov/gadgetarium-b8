package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.model.SubProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubProductRepository extends JpaRepository<SubProduct, Long> {
}