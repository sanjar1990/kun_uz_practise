package com.example.exceptions;

public class MethodNotAllowed extends RuntimeException{
    public MethodNotAllowed(String message){
        super(message);
    }
}
