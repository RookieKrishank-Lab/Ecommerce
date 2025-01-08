package com.ecommerce.project.payload;

import com.ecommerce.project.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponse {

    private List<ProductDTO> content;
}
