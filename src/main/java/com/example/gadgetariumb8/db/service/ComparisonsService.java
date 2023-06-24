package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.response.CompareCountResponse;
import com.example.gadgetariumb8.db.dto.response.CompareProductResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

import java.util.List;

public interface ComparisonsService {

    SimpleResponse saveOrDeleteComparisons(Long id, boolean addOrDelete);

    List<CompareProductResponse> compare(String categoryName);

    CompareCountResponse countCompare();

    SimpleResponse cleanCompare();
}
