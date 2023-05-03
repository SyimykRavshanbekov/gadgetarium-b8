package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
