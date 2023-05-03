package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.MailingListRequest;
import com.example.gadgetariumb8.db.dto.request.MailingListSubscriberRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.MailingListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mailing_lists")
@Tag(name = "Mailing List API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MailingListApi {

    private final MailingListService mailingListService;

    @Operation(summary = "Sending mail list", description = "This method takes all subscribers from db and sends mailing list to them.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public SimpleResponse sendMail(@RequestBody @Valid MailingListRequest mallingList) {
        return mailingListService.sendEmail(mallingList);
    }

    @Operation(summary = "Subscribing", description = "This method is responsible for saving subscribers to db.")
    @PostMapping("/subscribe")
    public SimpleResponse subscribe(@RequestBody @Valid MailingListSubscriberRequest subscriber) {
        return mailingListService.subscribe(subscriber);
    }
}
