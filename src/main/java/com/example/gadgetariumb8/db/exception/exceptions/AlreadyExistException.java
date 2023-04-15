package com.example.gadgetariumb8.db.exception.exceptions;

public class AlreadyExistException extends RuntimeException {

    public AlreadyExistException(){
        super();
    }

    public AlreadyExistException(String msg){
        super(msg);
    }
}
