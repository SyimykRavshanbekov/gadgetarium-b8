package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(" select u from UserInfo u where u.email = ?1")
    Optional<UserInfo> findUserInfoByEmail(String email);
}