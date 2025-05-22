package com.urmila.ecommerce.exceptions;

import com.urmila.ecommerce.payload.APIResponse;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalExcepionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> myMethodArgumentNotValidException(MethodArgumentNotValidException me)
    {
        Map<String,String> response=new HashMap<String,String>();
        me.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName=((FieldError)error).getField();
            //  String fieldName= error.getObjectName();
            String message=error.getDefaultMessage();
            response.put(fieldName,message);
        });
        return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException re)
    {
        String message=re.getMessage();
        APIResponse apiResponse=new APIResponse(message,false);
        return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myAPIException(APIException re)
    {
        String message=re.getMessage();
        APIResponse apiResponse=new APIResponse(message,false);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST );

    }
}
