package com.ecommerce.project.service;

import com.ecommerce.project.dto.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO category);

    CategoryResponse getAllCategory(int pageNumber, int pageSize, String sortBy, String sortOrder);

    CategoryDTO deleteCategory(long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO);
}
