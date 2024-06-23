package com.plataform.courses.services.exceptions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NotPermissionImmutableData extends DataIntegrityViolationException {

    public NotPermissionImmutableData(String message) {
        super(message);
    }

}