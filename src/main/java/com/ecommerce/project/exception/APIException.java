package com.ecommerce.project.exception;

//this exception will be called when user/client made a mistake. like same categoryName
public class APIException extends RuntimeException{

    String message;

    public APIException(String message, Object... args) {
        super(String.format(message, args));
    }
}
