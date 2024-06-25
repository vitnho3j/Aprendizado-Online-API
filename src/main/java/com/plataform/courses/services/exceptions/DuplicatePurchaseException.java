package com.plataform.courses.services.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicatePurchaseException extends DataIntegrityViolationException {

    public DuplicatePurchaseException(String message) {
        super(message);
    }

}
