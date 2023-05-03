package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.InfographicsResponse;
import com.example.gadgetariumb8.db.service.InfographicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/infographics")
@RequiredArgsConstructor
@Tag(name = "Infographic Admin API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InfographicApi {
    private final InfographicService infographicService;

    @GetMapping("/infographic")
    @Operation(summary = "To infographics the order.",
            description = "This method for infographic of all ordered items which will take a period of time and calculate a report for that period.")
    @PreAuthorize("hasAuthority('ADMIN')")
    public InfographicsResponse infographics(@RequestParam(defaultValue = "day") String period) {
        return infographicService.getInfographics(period);
    }
}
