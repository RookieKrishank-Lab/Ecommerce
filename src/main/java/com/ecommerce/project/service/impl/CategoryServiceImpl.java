package com.ecommerce.project.service.impl;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.dto.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
//        category.setCategoryId(categoryId++);
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryName = categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryName !=null){
            throw new APIException("Category with name '%s' already exists", category.getCategoryName());
        }
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(categoryRepository.save(category), CategoryDTO.class);
    }

    //in sortBy we need to choose the field according to which we need to sort. In sortOrder we need to define dsc or asc
    public CategoryResponse getAllCategory(int pageNumber, int pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        //encapsulate the pagination(pageNumber, pageSize...)
        //representation of page
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize, sortByAndOrder);

        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        //getContent return the list of categories
        List<Category> categoryList = categoryPage.getContent();
        if(categoryList.isEmpty()){
            throw  new APIException("No category present");
        }
        List<CategoryDTO> categoryDTOS = categoryList.stream().map(category -> modelMapper.map(category,CategoryDTO.class)).toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalPages());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    public CategoryDTO deleteCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    /*public String deleteCategory(long categoryId) {
        List<Category> categoryList = categoryRepository.findAll();
        Category requiredCategory = categoryList.stream()
                .filter(category -> category.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category with Id "+categoryId +" is not available in the list"));

        categoryRepository.delete(requiredCategory);=
        return "Category with id "+categoryId+" deleted successfully";
    }

    public Category updateCategory(Category category) {
        List<Category> categoryList = categoryRepository.findAll();
        Optional<Category> optionalCategory = categoryList.stream()
        //Category optionalCategory = categoryList.stream()
                .filter(c -> c.getCategoryId().equals(category.getCategoryId()))
                .findFirst();
                //.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category with Id "+category.getCategoryId() +" is not available in the list"));

          //optionalCategory.setCategoryName(category.getCategoryName());
          //return optionalCategory;


        if(optionalCategory.isPresent()){
            //if it enters the if case means we have that particular id category. so to edit that category we need to first get it and then alter it
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            Category updatedCategory = categoryRepository.save(existingCategory);
            return updatedCategory;
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category with Id "+category.getCategoryId() +" is not available in the list");
        }
    }*/
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {

        Category savedCategory = categoryRepository.findById(categoryDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryDTO.getCategoryId()));

        Category category = modelMapper.map(categoryDTO, Category.class);
        savedCategory.setCategoryName(category.getCategoryName());
        Category updatedCategory = categoryRepository.save(savedCategory);

        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }

}
