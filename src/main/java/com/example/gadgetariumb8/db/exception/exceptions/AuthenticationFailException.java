package com.example.gadgetariumb8.db.exception.exceptions;

public class AuthenticationFailException extends RuntimeException{

    public AuthenticationFailException(){
        super();
    }

    public AuthenticationFailException(String msg){
        super(msg);
    }
}
