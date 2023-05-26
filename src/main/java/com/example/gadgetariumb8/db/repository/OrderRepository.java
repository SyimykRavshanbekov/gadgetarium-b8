package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.model.Order;
import com.example.gadgetariumb8.db.model.SubProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}