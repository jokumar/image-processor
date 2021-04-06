package com.geeks18.exceptions;

public class RequestValidationException extends ServiceException{
    private String id;
    private String desc;


    public RequestValidationException( String errorcode, String errorMessage) {
        super(errorcode,errorMessage);
        this.id = errorcode;
        this.desc = errorMessage;

    }

}
