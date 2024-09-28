package org.example.blog.service;

import lombok.RequiredArgsConstructor;
import org.example.blog.dto.CategoryDTO;
import org.example.blog.entity.Category;
import org.example.blog.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    private Category convertToEntity(CategoryDTO dto) {
        return modelMapper.map(dto, Category.class);
    }

    private CategoryDTO convertToDto(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }

    public List<CategoryDTO> convertToDto(List<Category> categories) {
        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    public List<Category> convertToEntity(List<CategoryDTO> dtos) {
        return dtos.stream()
                .map(dto -> modelMapper.map(dto, Category.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO saveCategory(CategoryDTO category) {
        return convertToDto(categoryRepository.save(convertToEntity(category)));
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return convertToDto(categoryRepository.findAll());
    }

    @Override
    public CategoryDTO getCategoryById(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return convertToDto(category);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO category, long id) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);
        return convertToDto(existingCategory);
    }

    @Override
    public void deleteCategory(long id) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        categoryRepository.deleteById(id);
    }
}
