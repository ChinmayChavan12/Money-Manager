package com.Chinmay.MoneyManager.Service;

import com.Chinmay.MoneyManager.IO.CategoryDTO;
import com.Chinmay.MoneyManager.Model.CategoryEntity;
import com.Chinmay.MoneyManager.Model.ProfileEntity;

import java.util.List;

public interface CategoryService {

    CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profileEntity);
    CategoryDTO toDTO(CategoryEntity categoryEntity);
    CategoryDTO saveCategory(CategoryDTO categoryDTO);
    List<CategoryDTO> getCategoriesForCurrentUser();
    List<CategoryDTO> getAllCategoriesForCurrentUserByType(String type);
    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);
}
