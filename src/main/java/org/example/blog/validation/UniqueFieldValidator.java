package org.example.blog.validation;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueFieldValidator implements ConstraintValidator<UniqueField, String> {
    private final EntityManager entityManager;

    private Class<?> entityClass;
    private String field;

    @Override
    public void initialize(UniqueField constraintAnnotation) {
        this.entityClass = constraintAnnotation.entity();
        this.field = constraintAnnotation.field();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // Skip validation if the field is blank
        }

        // Check if the value exists in the database
        Long count = (Long) entityManager.createQuery("SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e WHERE e." + field + " = :value")
                .setParameter("value", value)
                .getSingleResult();

        return count == 0;
    }
}
