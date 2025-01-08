package com.ecommerce.project.service.impl;

import com.ecommerce.project.dto.ProductDTO;
import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;
import com.ecommerce.project.service.ProductService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
        Product product = modelMapper.map(productDTO, Product.class);
        product.setProductImage("default.png");
        product.setCategory(category);
        double specialPrice = product.getPrice() -((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
        productRepository.save(product);

        return modelMapper.map(product, ProductDTO.class);
    }

    public ProductResponse getAllProduct() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    public ProductResponse searchByCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchByKeyword(String keyword) {

        List<Product> products = productRepository.findByProductNameContainingIgnoreCase(keyword);
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        if(products.isEmpty()){
            throw new APIException("Products not found with keyword: " + keyword);
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    public ProductDTO updateProduct(ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productDTO.getProductId()));

        product.setProductName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setQuantity(productDTO.getQuantity());
        product.setPrice(productDTO.getPrice());
        product.setDiscount(productDTO.getDiscount());
        double specialPrice = productDTO.getPrice() -((productDTO.getDiscount() * 0.01) * productDTO.getPrice());
        product.setSpecialPrice(specialPrice);

        Product updatedProduct = productRepository.save(product);

        ProductDTO updatedProductDTO = modelMapper.map(updatedProduct, ProductDTO.class);
        return updatedProductDTO;
    }

    public ProductDTO deleteProduct(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productRepository.delete(product);
        return modelMapper.map(product,ProductDTO.class);
    }
}
