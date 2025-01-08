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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

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

    public ProductDTO updateProductImage(long productId, MultipartFile image) throws IOException {
        //get the product from DB
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        //upload the file to server (here we are storing the product image in an images folder)
        //get the file name of the server
        String path = "images/";
        String fileName = uploadImage(path, image);     //generic upload image method, which can upload any image on any path

        //updating the new file name to the product
        productFromDb.setProductImage(fileName);

        //save the updated product
        Product updatedProduct = productRepository.save(productFromDb);

        //map product to DTO
        return modelMapper.map(updatedProduct,ProductDTO.class);
    }

    private String uploadImage(String path, MultipartFile file) throws IOException {
        //Get file name of current / original file
        String originalFileName = file.getOriginalFilename();

        //Generate a unique file name( while uploading there is a change of name conflict then we will override some image )
        String randomId = UUID.randomUUID().toString();
        //mat.jpg -> 123(randomId) -> 123.jpg
        String fileName = randomId.concat(originalFileName.substring(originalFileName.indexOf(".")));
        String filePath = path + File.separator + fileName;              //new folder file path. Instead of File.separator we can use "/" but then it wont work on some OS
        //Check if path exist or create a new one
        File folder = new File(path);
        if(!folder.exists())
            folder.mkdir();

        //upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));         //(input file, destination of file)

        //Return file name
        return fileName;
    }
}
