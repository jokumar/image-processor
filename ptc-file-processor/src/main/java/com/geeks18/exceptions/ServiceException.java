package com.geeks18.exceptions;

import org.openapitools.model.Error;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceException extends RuntimeException{

    public List<Error> errors =new ArrayList<>();
    public List<Error> getErrors(){ return Collections.unmodifiableList(errors);}
    public ServiceException(String code, String description){

        Error error =new Error();
        error.setErrorCode(code);
        error.setErrrorDescription(description);
        this.errors.add(error);
    }

}
