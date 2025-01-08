package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.dto.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/public/v1/category")
public class CategoryController {

    private CategoryService categoryService;


    @GetMapping("/getAllCategory")
        public ResponseEntity<CategoryResponse> getAllCategory(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
                                                               @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
                                                               @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORY_BY, required = false) String sortBy,
                                                               @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION_ORDER, required = false) String sortOrder){
        CategoryResponse allCategory = categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(allCategory, HttpStatus.OK);
    }

    @PostMapping("/createCategory")
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>("Category create successfully",HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteCategory/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable long categoryId){
        CategoryDTO categoryDTO = categoryService.deleteCategory(categoryId);
            //static response
            return new ResponseEntity<>(categoryDTO,HttpStatus.OK);

    }

    @PutMapping("updateCategory/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @PathVariable long categoryId,
                                                 @RequestBody CategoryDTO categoryDTO){
        categoryDTO.setCategoryId(categoryId);
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryDTO);
        return new ResponseEntity<>(updatedCategory,HttpStatus.OK);

    }
}
