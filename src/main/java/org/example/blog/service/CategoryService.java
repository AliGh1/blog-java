package org.example.blog.service;

import org.example.blog.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO saveCategory(CategoryDTO category);
    List<CategoryDTO> getAllCategories(int size, int page);
    CategoryDTO getCategoryById(long id);
    CategoryDTO updateCategory(CategoryDTO category, long id);
    void deleteCategory(long id);
}
