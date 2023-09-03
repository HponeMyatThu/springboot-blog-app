package com.springboot.blog.expection;

import com.springboot.blog.payload.ErrorDetail;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // handle specific exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetail> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            WebRequest webRequest
    )
    {

        ErrorDetail errorDetail = new ErrorDetail(
                new Date(),
                exception.getMessage(),
                webRequest.getDescription(false)
        );

        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BlogAPIException.class)
    public ResponseEntity<ErrorDetail> handleBlogAPIException(
            BlogAPIException exception,
            WebRequest webRequest
    )
    {

        ErrorDetail errorDetail = new ErrorDetail(
                new Date(),
                exception.getMessage(),
                webRequest.getDescription(false)
        );

        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }


    // global exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> handleGlobalException(
            Exception exception,
            WebRequest webRequest
    )
    {

        ErrorDetail errorDetail = new ErrorDetail(
                new Date(),
                exception.getMessage(),
                webRequest.getDescription(false)
        );

        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    )
    {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();

            errors.put(fieldName, message);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /*
    Same with override
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            WebRequest webRequest
    )
    {

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();

            errors.put(fieldName, message);
        });


        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }*/
}
