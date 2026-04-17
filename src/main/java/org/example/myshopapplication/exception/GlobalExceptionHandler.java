package org.example.myshopapplication.exception;

import jakarta.validation.ConstraintViolationException;
import org.example.myshopapplication.enums.MethodTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<String> handleClientException(GenericException genericException) {
        return ResponseEntity
                .status(genericException.getStatus())
                .body(genericException.getMessage());
    }

    // Catch @Valid errors
    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>   handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {

        Map<String, String> result = new HashMap<>();

        methodArgumentNotValidException.getBindingResult().getFieldErrors().forEach(error ->
                result.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(result);

    }

    // Catch @Validated errors
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>>  handleConstraintViolationException(ConstraintViolationException constraintViolationException) {
        Map<String, String> result = new HashMap<>();

        constraintViolationException.getConstraintViolations().forEach(error ->
                result.put(error.getPropertyPath().toString(), error.getMessage()));
        return ResponseEntity.badRequest().body(result);

    }


    // Catch  errors for invalid methodType populated in  body request
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidEnum(HttpMessageNotReadableException httpMessageNotReadableException) {
           String exMessage = httpMessageNotReadableException.getMessage();
           List<String>  methodTypeJSONParsingErrors  = List.of("JSON parse error", "enums.MethodTypes");
           if(methodTypeJSONParsingErrors.stream().allMatch(exMessage::contains)) {
               return ResponseEntity
                       .badRequest()
                       .body("Please select valid MethodTypes :" + Arrays.toString(MethodTypes.values()));
           }

        return ResponseEntity
                .badRequest()
                .body(httpMessageNotReadableException.getMessage());
    }

    // In case spring cannot convert requestParams from request
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException missingServletRequestParameterException) {
        return ResponseEntity.badRequest()
                .body("Missing parameter: " + missingServletRequestParameterException.getParameterName());
    }


     // Catch  errors for invalid methodType populated in ParamRequest
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMissingParams(MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {


       if("methodType".equals(methodArgumentTypeMismatchException.getName())) {
           return ResponseEntity.badRequest()
                   .body("The selected methodType " + methodArgumentTypeMismatchException.getValue() + " is not from the valid MethodTypes :" + Arrays.toString(MethodTypes.values()));
       }

        return ResponseEntity.badRequest()
                .body(methodArgumentTypeMismatchException.getMessage());
    }

}
