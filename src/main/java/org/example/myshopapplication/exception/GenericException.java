package org.example.myshopapplication.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GenericException extends RuntimeException  {

    private final HttpStatus status;

    public GenericException(String errorMessage, HttpStatus status) {
        super(errorMessage);
        this.status = status;
    }

}
