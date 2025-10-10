package com.example.FoodOrderApp.category.services;

import com.example.FoodOrderApp.category.dtos.CategoryDTO;
import com.example.FoodOrderApp.response.Response;

import java.util.List;

public interface CategoryService {

    Response <CategoryDTO> addCategory(CategoryDTO categoryDTO);
    Response <CategoryDTO> updateCategory(CategoryDTO categoryDTO);
    Response <CategoryDTO> getCategoryById(Long id);
    Response <List< CategoryDTO>> getAllCategories();
    Response <?> deleteCategory(Long id);

}
