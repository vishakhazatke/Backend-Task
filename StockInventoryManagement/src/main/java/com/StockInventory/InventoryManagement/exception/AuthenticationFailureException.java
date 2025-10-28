package com.StockInventory.InventoryManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class AuthenticationFailureException extends RuntimeException{
    public AuthenticationFailureException(String message){
        super(message);
    }

}