package com.urmila.ecommerce.service;

import com.urmila.ecommerce.Model.Category;
import com.urmila.ecommerce.Model.Product;
import com.urmila.ecommerce.Repository.CategoryRepository;
import com.urmila.ecommerce.Repository.ProductRepository;
import com.urmila.ecommerce.exceptions.APIException;
import com.urmila.ecommerce.exceptions.ResourceNotFoundException;
import com.urmila.ecommerce.payload.ProductDTO;
import com.urmila.ecommerce.payload.ProductResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    CategoryRepository  categoryRepository;
    @Autowired
    ModelMapper modelMapper;

    public ProductDTO createProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Catgeory", "categoryId", categoryId));
        boolean ifProductNotPresent=true;
        List<Product>   products=category.getProducts();
        for(Product product:products){
            if(product.getProductId().equals(productDTO.getProductId())){
                ifProductNotPresent=false;
                break;
            }
        }
        if(ifProductNotPresent) {
            Product product = modelMapper.map(productDTO, Product.class);
            product.setCategory(category);
            double specialPrice = (product.getDiscount() * 0.01) * product.getPrice();
            product.setSpecialPrice(specialPrice);
            product.setImage("abc.jpg");
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        }
        else{
            throw new APIException("Product already exists");
        }

    }

    public ProductResponse getAllProducts(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> requestedPage=productRepository.findAll(pageable);
        List<Product> products=requestedPage.getContent();
        if(products.isEmpty())
            throw new APIException("No Products Found");
        List<ProductDTO> productDTOLIst=products.stream().map(product-> modelMapper.map(product, ProductDTO.class)).toList();
        if(productDTOLIst.isEmpty())
            throw new APIException("NO Products are Present");
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOLIst);
        productResponse.setPageNumber(requestedPage.getNumber());
        productResponse.setPageSize(requestedPage.getSize());
        productResponse.setTotalElements((int) requestedPage.getTotalElements());
        productResponse.setTotalPages(requestedPage.getTotalPages());
        productResponse.setLastPage(requestedPage.isLast());
        return productResponse;

    }

    public ProductResponse getProductsByCategory(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder)
    {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        Page<Product> requestedPage=productRepository.findByCategoryOrderByPriceAsc(category,pageable);
        List<Product> prod=requestedPage.getContent();
        List<ProductDTO> products=prod.stream().map(product-> modelMapper.map(product, ProductDTO.class)).toList();
        if(products.isEmpty())
            throw new APIException("NO Products are Present");
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(products);
        productResponse.setPageNumber(requestedPage.getNumber());
        productResponse.setPageSize(requestedPage.getSize());
        productResponse.setTotalElements((int) requestedPage.getTotalElements());
        productResponse.setTotalPages(requestedPage.getTotalPages());
        productResponse.setLastPage(requestedPage.isLast());
        return productResponse;
    }

    public ProductResponse getProductsByKeyword(String keyword,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> requestedPage=productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%',pageable);
        List<Product> prod=requestedPage.getContent();
        List<ProductDTO> byProductNameLikeIgnoreCase = prod.stream().map(product-> modelMapper.map(product, ProductDTO.class)).toList();
        if(byProductNameLikeIgnoreCase.isEmpty())
            throw new APIException("NO Products are Present");
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(byProductNameLikeIgnoreCase);
        productResponse.setPageNumber(requestedPage.getNumber());
        productResponse.setPageSize(requestedPage.getSize());
        productResponse.setTotalElements((int) requestedPage.getTotalElements());
        productResponse.setTotalPages(requestedPage.getTotalPages());
        productResponse.setLastPage(requestedPage.isLast());
        return productResponse;
    }

    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product product=modelMapper.map(productDTO, Product.class);
       Product productFromDB= productRepository.findById(productId).orElseThrow(()->new APIException("No Product Found"));
       productFromDB.setProductName(product.getProductName());
       productFromDB.setPrice(product.getPrice());
       productFromDB.setImage(product.getImage());
       productFromDB.setDescription(product.getDescription());
       productFromDB.setQuantity(product.getQuantity());
       productFromDB.setSpecialPrice(product.getSpecialPrice());
       Product productsaved=productRepository.save(productFromDB);
       return modelMapper.map(productsaved, ProductDTO.class);
    }

    public ProductDTO deleteProduct(ProductDTO productDTO, Long productId) {
        Product product=modelMapper.map(productDTO, Product.class);
        Product productFromDB= productRepository.findById(productId).orElseThrow(()->new APIException("No Product Found"));
        productRepository.delete(productFromDB);
        return modelMapper.map(productFromDB, ProductDTO.class);
    }

    public ProductDTO updateProductImage(Long productId, MultipartFile image) {
        Product product=productRepository.findById(productId).orElseThrow(()->new APIException("No Product found"));
        //upload to server
        String path="images/";
        //get the file name of uploaded image on server
        String fileName=uploadImage(path,image);
        product.setImage(fileName);
        Product productUpdate=productRepository.save(product);
        return modelMapper.map(productUpdate, ProductDTO.class);
    }

    private String uploadImage(String path, MultipartFile image) {
        String originalFileName=image.getOriginalFilename();
        System.out.println("file name :"+originalFileName);
        String randomId= UUID.randomUUID().toString();
        System.out.println(randomId);
        String fileName=randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));
        System.out.println(fileName);
        String filePath= path+ File.pathSeparator+fileName;
        File folder= new File(filePath);
        //check if path exist and create
        if(!folder.exists())
        {
            folder.mkdir();
        }
        //upload files to server
        try {
            Files.copy(image.getInputStream(), Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }
}
