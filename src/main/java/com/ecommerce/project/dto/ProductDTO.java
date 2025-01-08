package com.ecommerce.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private long productId;
    private String productName;
    private String productImage;
    private String description;
    private int quantity;
    private double price;
    private double discount;
    private double specialPrice;

}
