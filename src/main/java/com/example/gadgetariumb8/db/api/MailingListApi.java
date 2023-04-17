package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.MailingListRequest;
import com.example.gadgetariumb8.db.dto.request.MailingListSubscriberRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.model.MallingList;
import com.example.gadgetariumb8.db.model.MallingListSubscriber;
import com.example.gadgetariumb8.db.service.impl.MailingListServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mailing_lists")
@Tag(name = "Mailing List API")
public class MailingListApi {

    private final MailingListServiceImpl mailingListService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Sending mail list", description = "This method takes all subscribers from db and sends mailing list to them.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public SimpleResponse sendMail(@RequestBody @Valid MailingListRequest mallingList) {
        return mailingListService.sendEmail(modelMapper.map(mallingList, MallingList.class));
    }

    @Operation(summary = "Subscribing", description = "This method is responsible for saving subscribers to db.")
    @PostMapping("/subscribe")
    public SimpleResponse subscribe(@RequestBody @Valid MailingListSubscriberRequest subscriber) {
        return mailingListService.subscribe(modelMapper.map(subscriber, MallingListSubscriber.class));
    }
}
