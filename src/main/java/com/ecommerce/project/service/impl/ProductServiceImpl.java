package com.ecommerce.project.service.impl;

import com.ecommerce.project.dto.ProductDTO;
import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;
import com.ecommerce.project.service.FileService;
import com.ecommerce.project.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

//@AllArgsConstructor
//@NoArgsConstructor
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

//    @Autowired
    private final ProductRepository productRepository;
//    @Autowired
    private final CategoryRepository categoryRepository;
//    @Autowired
    private final ModelMapper modelMapper;
//    @Autowired
    private FileService fileService;

    @Value("${product.image}")
    private String path;

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

    public ProductDTO updateProductImage(long productId, MultipartFile image) throws IOException {
        //get the product from DB
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        //upload the file to server (here we are storing the product image in an images folder)
        //get the file name of the server
        String fileName = fileService.uploadImage(path, image);     //generic upload image method, which can upload any image on any path. path value we will take from properties file

        //updating the new file name to the product
        productFromDb.setProductImage(fileName);

        //save the updated product
        Product updatedProduct = productRepository.save(productFromDb);

        //map product to DTO
        return modelMapper.map(updatedProduct,ProductDTO.class);
    }

}
