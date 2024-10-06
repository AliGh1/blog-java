package org.example.blog.dto;

import org.example.blog.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(RegisterRequestDTO dto);
}
