package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}