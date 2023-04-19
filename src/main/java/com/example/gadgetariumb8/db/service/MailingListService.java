package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.MailingListRequest;
import com.example.gadgetariumb8.db.dto.request.MailingListSubscriberRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

public interface MailingListService {
    SimpleResponse sendEmail(MailingListRequest mallingList);

    SimpleResponse subscribe(MailingListSubscriberRequest subscriber);
}

