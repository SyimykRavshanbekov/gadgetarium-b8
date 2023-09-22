package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UserInfo> findByResetPasswordToken(String token);

    boolean existsByEmailAndIdNot(String email, Long id);

}

