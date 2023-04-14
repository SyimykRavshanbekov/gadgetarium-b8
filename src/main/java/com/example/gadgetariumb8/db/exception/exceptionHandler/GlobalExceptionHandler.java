package com.example.gadgetariumb8.db.exception.exceptionHandler;

import com.example.gadgetariumb8.db.exception.exceptions.AlreadyExistException;
import com.example.gadgetariumb8.db.exception.exceptions.AuthenticationFailException;
import com.example.gadgetariumb8.db.exception.exceptionResponse.ExceptionResponse;
import com.example.gadgetariumb8.db.exception.exceptions.BadCredentialException;
import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(AuthenticationFailException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResponse authenticationFail(AuthenticationFailException exception){
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .exceptionClassName(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build();
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ExceptionResponse notFound(NotFoundException exception){
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .exceptionClassName(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build();
    }


    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResponse alreadyExist(AlreadyExistException exception){
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .exceptionClassName(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build();
    }


//    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResponse methodArgumentNotValid(MethodArgumentNotValidException exception){
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .exceptionClassName(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build();
    }


    @ExceptionHandler(BadCredentialException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResponse badCredential(BadCredentialException exception){
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .exceptionClassName(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build();
    }

}
