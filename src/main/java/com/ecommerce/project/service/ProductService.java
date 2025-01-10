package com.ecommerce.project.service;

import com.ecommerce.project.dto.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(long categoryId, ProductDTO product);

    ProductResponse getAllProduct(int pageNumber, int pageSize, String sortBy, String sortOrder);

    ProductResponse searchByCategory(long categoryId, int pageNumber, int pageSize, String sortBy, String sortOrder);

    ProductResponse searchByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortOrder);

    ProductDTO updateProduct(ProductDTO productDTO);

    ProductDTO deleteProduct(long productId);

    ProductDTO updateProductImage(long productId, MultipartFile image) throws IOException;
}
