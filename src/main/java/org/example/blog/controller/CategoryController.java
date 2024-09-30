package org.example.blog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.blog.dto.CategoryDTO;
import org.example.blog.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> index(@RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "0") int page) {
        return categoryService.getAllCategories(size, page);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> store(@RequestBody @Valid CategoryDTO category) {

        return new ResponseEntity<>(categoryService.saveCategory(category), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryDTO> show(@PathVariable("id") long id) {
        return new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable("id") long id, @RequestBody @Valid CategoryDTO category) {
        return new ResponseEntity<>(categoryService.updateCategory(category, id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> destroy(@PathVariable("id") long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("Category deleted Successfully.", HttpStatus.OK);
    }
}
