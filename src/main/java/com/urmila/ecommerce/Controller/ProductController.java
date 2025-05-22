package com.urmila.ecommerce.Controller;

import com.urmila.ecommerce.Model.Product;
import com.urmila.ecommerce.constants.Appconstants;
import com.urmila.ecommerce.payload.ProductDTO;
import com.urmila.ecommerce.payload.ProductResponse;
import com.urmila.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api")
@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(name = "pageNumber", defaultValue = Appconstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                          @RequestParam(name = "pageSize", defaultValue = Appconstants.PAGE_SIZE, required = false) Integer pageSize,
                                                          @RequestParam(name = "sortBy", defaultValue = Appconstants.SORT_BY_PRODUCT, required = false) String sortBy,
                                                          @RequestParam(name = "sortOrder", defaultValue = Appconstants.SORT_ORDER, required = false) String sortOrder)
    {
        ProductResponse productResponse = productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/public/category/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,@RequestParam(name = "pageNumber", defaultValue = Appconstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = Appconstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = Appconstants.SORT_BY_PRODUCT, required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = Appconstants.SORT_ORDER, required = false) String sortOrder)
    {
        ProductResponse productResponse=productService.getProductsByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);
        return ResponseEntity.ok(productResponse);
    }
    @GetMapping("/public/products/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,@RequestParam(name = "pageNumber", defaultValue = Appconstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = Appconstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue = Appconstants.SORT_BY_PRODUCT, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder", defaultValue = Appconstants.SORT_ORDER, required = false) String sortOrder)
    {
        ProductResponse productResponseList=productService.getProductsByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
        return ResponseEntity.ok(productResponseList);
    }


    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDtO, @PathVariable Long categoryId) {
        ProductDTO savedProductDTO=productService.createProduct(productDtO,categoryId);
        return ResponseEntity.ok(savedProductDTO);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDtO, @PathVariable Long productId)
    {
        ProductDTO productDTO=productService.updateProduct(productDtO,productId);
        return ResponseEntity.ok(productDTO);
    }
    @PutMapping(value="/products/{productId}/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> updateImage(@PathVariable Long productId, @RequestPart MultipartFile image)
    {
        ProductDTO productDTO=productService.updateProductImage(productId,image);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }
    @DeleteMapping ("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@RequestBody ProductDTO productDtO, @PathVariable Long productId) {
        ProductDTO productDTO=productService.deleteProduct(productDtO,productId);
        return ResponseEntity.ok(productDTO);
    }
}
