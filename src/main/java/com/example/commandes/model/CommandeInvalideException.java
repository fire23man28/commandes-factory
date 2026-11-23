package com.example.commandes.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CommandeInvalideException extends RuntimeException {

    public CommandeInvalideException(String message) {
        super(message);
    }
}
