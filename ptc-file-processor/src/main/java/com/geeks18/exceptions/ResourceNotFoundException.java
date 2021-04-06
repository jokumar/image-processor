package com.geeks18.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ServiceException {
    private String id;
    private String desc;


    public ResourceNotFoundException( String errorcode, String errorMessage) {
        super(errorcode,errorMessage);
        this.id = errorcode;
        this.desc = errorMessage;

    }

}