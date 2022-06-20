package com.example.blogapi.service;

import com.example.blogapi.entity.Category;
import com.example.blogapi.exceptions.ResourceNotFoundException;
import com.example.blogapi.mapper.CategoryMapper;
import com.example.blogapi.payloads.CategoryDto;
import com.example.blogapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    public CategoryDto createCategory(CategoryDto categoryDto){
        Category category=categoryMapper.DtoToEntity(categoryDto);
        Category savedCategory=categoryRepository.save(category);
        return categoryMapper.EntityToDto(savedCategory);
    }

    public CategoryDto updateCategory(CategoryDto categoryDto,Long categoryId){
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","Category ID",categoryId));
        category.setCategoryTitle(categoryDto.getCategoryTitle());
        category.setCategoryDescription(categoryDto.getCategoryDescription());

        Category updatedCategory=categoryRepository.save(category);

        return categoryMapper.EntityToDto(updatedCategory);
    }

    public void deleteCategory(Long categoryId){
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","Category ID",categoryId));
        categoryRepository.delete(category);

    }

    public CategoryDto getCategory(Long categoryId){
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","Category ID",categoryId));
        return categoryMapper.EntityToDto(category);
    }

    public List<CategoryDto> getAllCategory(){
        return categoryRepository.findAll().stream()
                .map(categoryMapper::EntityToDto)
                .collect(Collectors.toList());
    }

}
