package com.urmila.ecommerce.service;

import com.urmila.ecommerce.Model.Category;
import com.urmila.ecommerce.Repository.CategoryRepository;
import com.urmila.ecommerce.exceptions.APIException;
import com.urmila.ecommerce.exceptions.ResourceNotFoundException;
import com.urmila.ecommerce.payload.CategoryDTO;
import com.urmila.ecommerce.payload.CategoryResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CategoryResponse  getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> requestedpage=categoryRepository.findAll(pageable);
        List<Category> categoryList=requestedpage.getContent();
        if(categoryList.isEmpty())
            throw new APIException("NO Categories are Present");
        List<CategoryDTO> categoryDTO= categoryList.stream().map(category->modelMapper.map(category,CategoryDTO.class)).toList();
        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setContent(categoryDTO);
        categoryResponse.setPageNumber(requestedpage.getNumber());
        categoryResponse.setPageSize(requestedpage.getSize());
        categoryResponse.setTotalElements((int) requestedpage.getTotalElements());
        categoryResponse.setTotalPages(requestedpage.getTotalPages());
        categoryResponse.setLastPage(requestedpage.isLast());
        return categoryResponse;
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category=modelMapper.map(categoryDTO,Category.class);
        Category saved=categoryRepository.findByCategoryName(category.getCategoryName());
        if(saved!=null)
            throw new APIException("Category with name "+category.getCategoryName()+" already exists");
        Category savedcategory=categoryRepository.save(category);
        return modelMapper.map(savedcategory,CategoryDTO.class);


    }

    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Catgeory","categoryId",categoryId));
        categoryRepository.delete(category);
        return modelMapper.map(category,CategoryDTO.class);
    }

    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Category category=modelMapper.map(categoryDTO,Category.class);
        Category savetoCategory = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Catgeory","categoryId",categoryId));
        savetoCategory.setCategoryName(category.getCategoryName());
        Category updatedCategory=categoryRepository.save(savetoCategory);
        return modelMapper.map(updatedCategory,CategoryDTO.class);

    }
}
