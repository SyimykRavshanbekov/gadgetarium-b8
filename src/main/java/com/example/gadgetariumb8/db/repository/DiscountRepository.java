package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}