package com.plataform.courses.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.persistence.EntityNotFoundException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadWordException extends EntityNotFoundException {

    public BadWordException(String message) {
        super(message);
    }

}
