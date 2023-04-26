package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
