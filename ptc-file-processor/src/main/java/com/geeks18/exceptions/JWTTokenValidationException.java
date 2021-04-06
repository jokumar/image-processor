package com.geeks18.exceptions;

public class JWTTokenValidationException extends ServiceException{
    String errorcode;
    String errorMessage;
    public JWTTokenValidationException(String errorcode, String errorMessage){
        super(errorcode,errorMessage);
        this.errorcode=errorcode;
        this.errorMessage=errorMessage;

    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
