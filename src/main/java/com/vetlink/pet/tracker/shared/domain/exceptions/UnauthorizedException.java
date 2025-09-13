package com.vetlink.pet.tracker.shared.domain.exceptions;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(){
        super();
    }

    public UnauthorizedException(String message){
        super(message);
    }
}