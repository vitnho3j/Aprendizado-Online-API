package com.plataform.courses.services.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadWordException extends DataIntegrityViolationException {

    public BadWordException(String message) {
        super(message);
    }

}
