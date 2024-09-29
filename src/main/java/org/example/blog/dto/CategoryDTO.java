package org.example.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.blog.entity.Category;
import org.example.blog.validation.UniqueField;


@Getter
@Setter
public class CategoryDTO {
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    @UniqueField(entity = Category.class, field = "name", message = "Name must be unique")
    private String name;
}
