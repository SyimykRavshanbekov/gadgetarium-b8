package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.ContactRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.model.Contact;
import com.example.gadgetariumb8.db.repository.ContactRepository;
import com.example.gadgetariumb8.db.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRepository repository;
    @Override
    public SimpleResponse save(ContactRequest request) {
        Contact contact = new Contact();
        contact.setFirstName(request.firstName());
        contact.setLastName(request.lastName());
        contact.setEmail(request.email());
        contact.setPhoneNumber(request.phoneNumber());
        contact.setMessage(request.message());
        repository.save(contact);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Contact is successfully saved!").build();
    }
}
