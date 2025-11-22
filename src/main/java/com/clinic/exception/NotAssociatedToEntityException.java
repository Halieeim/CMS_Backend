package com.clinic.exception;

public class NotAssociatedToEntityException extends RuntimeException{
    public NotAssociatedToEntityException(String message){
        super(message);
    }
}

