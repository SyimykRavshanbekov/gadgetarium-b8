package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.catalog.CatalogProductsResponse;
import com.example.gadgetariumb8.db.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Search API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SearchApi {
    private final SearchService service;

    @GetMapping()
    @Operation(summary = "Search", description = "This method gets all products by searching.")
    public List<CatalogProductsResponse> search(@RequestParam(value = "keyword", required = false) String keyword) {
        return service.searchByKeyword(keyword);
    }
}
