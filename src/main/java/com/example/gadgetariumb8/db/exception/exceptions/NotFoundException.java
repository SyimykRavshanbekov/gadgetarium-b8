package com.example.gadgetariumb8.db.exception.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(){
        super();
    }

    public NotFoundException(String msg){
        super(msg);
    }
}
