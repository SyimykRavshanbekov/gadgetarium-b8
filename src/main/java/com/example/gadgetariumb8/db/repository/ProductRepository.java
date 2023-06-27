package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Modifying
    @Query(nativeQuery = true, value = "delete from products p where p.id = ?1")
    void deleteProductById(Long id);
}