package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.model.MallingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailingListRepository extends JpaRepository<MallingList, Long> {
}
