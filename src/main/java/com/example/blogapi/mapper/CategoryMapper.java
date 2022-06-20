package com.example.blogapi.mapper;

import com.example.blogapi.entity.Category;
import com.example.blogapi.payloads.CategoryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public abstract class CategoryMapper {

    public abstract Category DtoToEntity(CategoryDto categoryDto);

    public abstract CategoryDto EntityToDto(Category category);
}
