package com.ecommerce.project.controller;

import com.ecommerce.project.dto.CategoryDTO;
import com.ecommerce.project.dto.ProductDTO;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private ProductService productService;

    @PostMapping("/admin/addProduct/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO,
                                                 @PathVariable long categoryId){

        ProductDTO savedProduct = productService.addProduct(categoryId, productDTO);

        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAll(){
        ProductResponse productResponse = productService.getAllProduct();
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/product")
    public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable long categoryId){
        ProductResponse productResponse = productService.searchByCategory(categoryId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword){
        ProductResponse productResponse = productService.searchByKeyword(keyword);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @PutMapping("/admin/updateProduct/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable long productId,
                                                    @RequestBody ProductDTO productDTO){
        productDTO.setProductId(productId);
        ProductDTO updateProductDTO = productService.updateProduct(productDTO);
        return new ResponseEntity<>(updateProductDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable long productId){
        ProductDTO deletedProduct = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }
}
