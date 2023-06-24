package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.CompareCountResponse;
import com.example.gadgetariumb8.db.dto.response.CompareProductResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.ComparisonsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comparisons")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Comparisons Api")
public class ComparisonsApi {
    private final ComparisonsService comparisonsService;

    @PostMapping
    @Operation(summary = "Add or delete from comparisons", description = "This method to add or delete from comparisons")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse saveComparisons(@RequestParam Long id, @RequestParam boolean addOrDelete) {
        return comparisonsService.saveOrDeleteComparisons(id, addOrDelete);
    }

    @GetMapping("/compare-product")
    @Operation(summary = "To compare the product.", description = "This method to compare product.")
    @PreAuthorize("hasAuthority('USER')")
    public List<CompareProductResponse> compareProduct(@RequestParam String categoryName) {
        return comparisonsService.compare(categoryName);
    }

    @GetMapping("/countCompare")
    @Operation(summary = "To count the Compare.", description = "This method count the Compare")
    @PreAuthorize("hasAuthority('USER')")
    public CompareCountResponse countCompare() {
        return comparisonsService.countCompare();
    }

    @DeleteMapping
    @Operation(summary = "To clean the Compare", description = "This method clean table Comparisons")
    @PreAuthorize("hasAuthority('USER')")
    public SimpleResponse cleanCompare() {
        return comparisonsService.cleanCompare();
    }
}
