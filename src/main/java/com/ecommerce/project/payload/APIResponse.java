package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//instead of getting a string while getting an exception we will be using this class so that in return we get an output when we counter an exception
public class APIResponse {

    private String message;
    private boolean status;

}
