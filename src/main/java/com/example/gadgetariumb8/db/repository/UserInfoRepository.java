package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author kurstan
 * @created at 13.04.2023 9:46
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByEmail(String email);
    boolean existsByEmail(String email);

}
