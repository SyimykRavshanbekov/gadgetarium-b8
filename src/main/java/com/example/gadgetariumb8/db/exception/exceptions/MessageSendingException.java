package com.example.gadgetariumb8.db.exception.exceptions;

public class MessageSendingException extends RuntimeException{
    public MessageSendingException(){
        super();
    }

    public MessageSendingException(String msg){
        super(msg);
    }
}
