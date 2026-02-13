package com.Chinmay.MoneyManager.ServiceImpl;

import com.Chinmay.MoneyManager.IO.CategoryDTO;
import com.Chinmay.MoneyManager.Model.CategoryEntity;
import com.Chinmay.MoneyManager.Model.ProfileEntity;
import com.Chinmay.MoneyManager.Repository.CategoryEntityRepository;
import com.Chinmay.MoneyManager.Service.CategoryService;
import com.Chinmay.MoneyManager.Service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final ProfileService profileService;
    private final CategoryEntityRepository categoryEntityRepository;


    @Override
    public CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profileEntity) {
        return CategoryEntity.builder()
                .categoryName(categoryDTO.getCategoryName())
                .type(categoryDTO.getType())
                .iconUrl(categoryDTO.getIconUrl())
                .profile(profileEntity)
                .build();
    }

    @Override
    public CategoryDTO toDTO(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .id(categoryEntity.getId())
                .profileId(categoryEntity.getProfile()!=null?categoryEntity.getProfile().getId():null)
                .categoryName(categoryEntity.getCategoryName())
                .iconUrl(categoryEntity.getIconUrl())
                .type(categoryEntity.getType())
                .build();
    }

    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        ProfileEntity profile=profileService.getCurrentProfile();
        if(categoryEntityRepository.existsByCategoryNameAndProfileId(categoryDTO.getCategoryName(), profile.getId())) {
            throw new RuntimeException("Category already exists");
        }
            CategoryEntity newCategory=toEntity(categoryDTO, profile);
        newCategory=categoryEntityRepository.save(newCategory);
        return toDTO(newCategory);
    }

    @Override
    public List<CategoryDTO> getCategoriesForCurrentUser() {
        ProfileEntity profile=profileService.getCurrentProfile();
        List<CategoryEntity> categoryEntities=categoryEntityRepository.findByProfileId(profile.getId());

        return categoryEntities.stream().map(this::toDTO).toList();
    }

    @Override
    public List<CategoryDTO> getAllCategoriesForCurrentUserByType(String type) {
        ProfileEntity profile=profileService.getCurrentProfile();
      List<CategoryEntity>entities= categoryEntityRepository.findByTypeAndProfileId(type,profile.getId());
       return entities.stream().map(this::toDTO).toList();
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId,CategoryDTO categoryDTO) {
        ProfileEntity profile=profileService.getCurrentProfile();
        CategoryEntity existingCategory= categoryEntityRepository.findByIdAndProfileId(categoryId,profile.getId())
                .orElseThrow(() -> new RuntimeException("Category Not Found or not accessible"));
        existingCategory.setCategoryName(categoryDTO.getCategoryName());
        existingCategory.setIconUrl(categoryDTO.getIconUrl());
        return toDTO(categoryEntityRepository.save(existingCategory));

    }


}
