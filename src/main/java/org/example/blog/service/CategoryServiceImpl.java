package org.example.blog.service;

import lombok.RequiredArgsConstructor;
import org.example.blog.dto.CategoryDTO;
import org.example.blog.dto.CategoryMapper;
import org.example.blog.entity.Category;
import org.example.blog.repository.CategoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;


    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        validationUniqueName(categoryDTO.getName());
        var category = mapper.toCategory(categoryDTO);
        var categorySaved = categoryRepository.save(category);
        return mapper.toCategoryDto(categorySaved);
    }

    @Override
    public List<CategoryDTO> getAllCategories(int size, int page) {
        if (size <= 0 || page < 0) {
            throw new IllegalArgumentException("Invalid pagination parameters");
        }
        PageRequest pageable = PageRequest.of(size, page);
        return categoryRepository.findAll(pageable).stream().map(mapper::toCategoryDto).toList();
    }

    @Override
    public CategoryDTO getCategoryById(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return mapper.toCategoryDto(category);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (existingCategory.getName().equals(categoryDTO.getName())) {
            return categoryDTO;
        }
        validationUniqueName(categoryDTO.getName());
        existingCategory.setName(categoryDTO.getName());
        var categoryUpdated = categoryRepository.save(existingCategory);
        return mapper.toCategoryDto(categoryUpdated);
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.findById(id).ifPresentOrElse(category -> {
            category.setDeleted(LocalDateTime.now());
            categoryRepository.save(category);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });
    }

    private void validationUniqueName(String name) {
        boolean exists = categoryRepository.existsByName(name);
        if (exists)
            throw new IllegalArgumentException("Name must be unique");

    }


}
