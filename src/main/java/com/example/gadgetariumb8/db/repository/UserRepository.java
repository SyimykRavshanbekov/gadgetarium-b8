package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.model.User;
import com.example.gadgetariumb8.db.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from UserInfo u where u.email = ?1")
    Optional<UserInfo> findUserInfoByEmail(String email);

    @Query("select u from User u join u.userInfo ui where ui.email = ?1")
    Optional<User> findUserByEmail(String email);

    @Modifying
    @Query(value = "delete from users_comparisons where comparisons_id= ?1 and user_id= ?2", nativeQuery = true)
    void deleteComparisonsId(Long subProductId, Long userId);
}