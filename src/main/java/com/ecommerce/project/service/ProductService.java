package com.ecommerce.project.service;

import com.ecommerce.project.dto.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(long categoryId, ProductDTO product);

    ProductResponse getAllProduct();

    ProductResponse searchByCategory(long categoryId);

    ProductResponse searchByKeyword(String keyword);

    ProductDTO updateProduct(ProductDTO productDTO);

    ProductDTO deleteProduct(long productId);

    ProductDTO updateProductImage(long productId, MultipartFile image) throws IOException;
}
