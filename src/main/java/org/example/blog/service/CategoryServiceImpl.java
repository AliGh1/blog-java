package org.example.blog.service;

import lombok.RequiredArgsConstructor;
import org.example.blog.dto.CategoryDTO;
import org.example.blog.dto.CategoryMapper;
import org.example.blog.entity.Category;
import org.example.blog.exception.CustomValidationException;
import org.example.blog.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    @Override
    public CategoryDTO saveCategory(CategoryDTO category) {
        validationUniqueName(category.getName());
        return mapper.toDTO(categoryRepository.save(mapper.toEntity(category)));
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public CategoryDTO getCategoryById(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return mapper.toDTO(category);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO category, long id) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (existingCategory.getName().equals(category.getName())) {
            return category;
        }
        validationUniqueName(category.getName());
        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);
        return mapper.toDTO(existingCategory);
    }

    @Override
    public void deleteCategory(long id) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        categoryRepository.deleteById(id);
    }

    private void validationUniqueName(String name) {
        if (categoryRepository.existsByName(name)) {
            Map<String, String> errors = new HashMap<>();
            errors.put("name", "name is already taken");
            throw new CustomValidationException(errors);
        }
    }
}
