package org.example.blog.dto;

import org.example.blog.entity.Category;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {
    CategoryDTO toCategoryDto(Category category);
    Category toCategory(CategoryDTO categoryDto);
}
