package com.ecommerce.project.exception;

import com.ecommerce.project.payload.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

/*
    // error while passing data/ improper data in the request body
    // for user-friendly purpose we are using Map
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){

        //we are using response to get a better error message in the output. with predefine msg, error message, time stamp
        //just like ErrorDetails class from ramesh project 1
        Map<String,Object> response = new HashMap<>();

        //map which only contain field and the error message
        Map<String, String> fieldErrors = new HashMap<>();
        // Logging the exception details
        logger.error("Validation error occurred: {}", e.getMessage());

        //retrieve a list of all the errors that are caught during the validation of method parameters.
        e.getBindingResult().getAllErrors().forEach(error->{
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            Object rejectedValue = ((FieldError) error).getRejectedValue();

            // Handle null rejected values
            if (rejectedValue == null) {
                rejectedValue = "null (or missing value)";
            }

            // Log each field's error
            logger.warn("Field: {}, Rejected Value: {}, Error: {}", fieldName, rejectedValue, message);

            fieldErrors.put(fieldName, message + ", Rejected Value: " + rejectedValue);
//            response.put(fieldName, message);
        });
        response.put("timestamp", System.currentTimeMillis());
        response.put("message", "Validation failed");
        response.put("errors", fieldErrors);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }*/

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){

        //map which only contain field and the error message
        Map<String, String> fieldErrors = new HashMap<>();
        // Logging the exception details
        logger.error("Validation error occurred: {}", e.getMessage());

        //retrieve a list of all the errors that are caught during the validation of method parameters.
        e.getBindingResult().getAllErrors().forEach(error->{
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            Object rejectedValue = ((FieldError) error).getRejectedValue();

            // Handle null rejected values
            if (rejectedValue == null || "".equals(rejectedValue)) {
                rejectedValue = "null (or missing value)";
            }

            // Log each field's error
            logger.warn("Field: {}, Rejected Value: {}, Error: {}", fieldName, rejectedValue, message);
            logger.info("Time of error: {}", System.currentTimeMillis());

            fieldErrors.put(fieldName, message + ", Rejected Value: " + rejectedValue);

        });
        return new ResponseEntity<Map<String, String>>(fieldErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e){
        String message = e.getMessage();
        APIResponse response = new APIResponse();
        response.setMessage(message);
        response.setStatus(false);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myAPIException(APIException e){
        String message = e.getMessage();
        APIResponse response = new APIResponse();
        response.setMessage(message);
        response.setStatus(false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
