package com.StockInventory.InventoryManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidInputException extends RuntimeException{
    public InvalidInputException(String message){
        super(message);
    }

}