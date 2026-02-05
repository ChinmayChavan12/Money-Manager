package com.Chinmay.MoneyManager.Repository;

import com.Chinmay.MoneyManager.Model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryEntityRepository extends JpaRepository<CategoryEntity, Integer> {
   List<CategoryEntity> findByProfileId(Long profileId);

   Optional<CategoryEntity> findByIdAndProfileId(Long id, Long profileId);

   List<CategoryEntity> findByTypeAndProfileId(String type,Long profileId);

   Boolean existsByNameAndProfileId(String name,Long profileId);
}
