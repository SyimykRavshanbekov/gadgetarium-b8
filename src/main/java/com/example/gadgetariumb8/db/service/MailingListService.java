package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.model.MallingList;
import com.example.gadgetariumb8.db.model.MallingListSubscriber;

public interface MailingListService {
    SimpleResponse sendEmail(MallingList mallingList);

    SimpleResponse subscribe(MallingListSubscriber subscriber);
}

