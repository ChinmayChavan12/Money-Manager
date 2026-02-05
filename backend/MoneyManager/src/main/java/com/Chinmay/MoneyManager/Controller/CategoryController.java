package com.Chinmay.MoneyManager.Controller;

import com.Chinmay.MoneyManager.IO.CategoryDTO;
import com.Chinmay.MoneyManager.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDTO categoryDTO){
        CategoryDTO saveCategoryDTO=categoryService.saveCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveCategoryDTO);
    }

    @GetMapping
    public ResponseEntity<?> getCategories(){
        List<CategoryDTO> categories=categoryService.getCategoriesForCurrentUser();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{type}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByTypeForCurrentUser(@PathVariable String type){
        List<CategoryDTO> list=categoryService.getAllCategoriesForCurrentUserByType(type);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId,@RequestBody CategoryDTO categoryDTO){

        CategoryDTO updatedCategory=  categoryService.updateCategory(categoryId,categoryDTO);
        return ResponseEntity.ok(updatedCategory);
    }
}
