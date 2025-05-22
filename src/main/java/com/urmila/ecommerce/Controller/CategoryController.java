package com.urmila.ecommerce.Controller;

import com.urmila.ecommerce.Model.Category;
import com.urmila.ecommerce.constants.Appconstants;
import com.urmila.ecommerce.payload.CategoryDTO;
import com.urmila.ecommerce.payload.CategoryResponse;
import com.urmila.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getCategoryList(@RequestParam(name = "pageNumber", defaultValue = Appconstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                            @RequestParam(name = "pageSize", defaultValue = Appconstants.PAGE_SIZE, required = false) Integer pageSize,
                                                            @RequestParam(name = "sortBy", defaultValue = Appconstants.SORT_BY, required = false) String sortBy,
                                                            @RequestParam(name = "sortOrder", defaultValue = Appconstants.SORT_ORDER, required = false) String sortOrder) {
        CategoryResponse allCategories = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(allCategories);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(savedCategoryDTO);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
        CategoryDTO status = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(status, HttpStatus.OK);

    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId) {

        CategoryDTO cate = categoryService.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(cate, HttpStatus.OK);

    }

}
