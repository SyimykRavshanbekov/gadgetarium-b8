package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.model.Category;
import com.example.gadgetariumb8.db.repository.CategoryRepository;
import com.example.gadgetariumb8.db.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
