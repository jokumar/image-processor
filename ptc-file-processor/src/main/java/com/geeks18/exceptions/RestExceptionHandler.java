package com.geeks18.exceptions;

import org.openapitools.model.Error;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;

@ControllerAdvice
public class RestExceptionHandler
        extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value
            = { JWTTokenValidationException.class,RequestValidationException.class })
    protected ResponseEntity<Object> handleResourceValidation(
            ServiceException ex, WebRequest request) {

        return handleExceptionInternal(ex, ex.getErrors(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value
            = { ResourceNotFoundException.class })
    protected ResponseEntity<Object> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {

        return handleExceptionInternal(ex, ex.getErrors(),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value
            = { MessageCommunicationException.class })
    protected ResponseEntity<Object> handleChannelCommunication(
            ResourceNotFoundException ex, WebRequest request) {

        return handleExceptionInternal(ex, ex.getErrors(),
                new HttpHeaders(), HttpStatus.BAD_GATEWAY, request);
    }


    @ExceptionHandler(value
            = { IllegalArgumentException.class })
    protected ResponseEntity<Object> handleIllegelaArgumentException(
            RuntimeException ex, WebRequest request) {
        logger.error(" IllegalArgumentException "+ ex);
        ArrayList errors=new ArrayList();
        Error error=new Error();
        error.setErrorCode(ErrorConstants.ERR_5_CD);
        error.setErrrorDescription(ex.getMessage());
        errors.add(error);
        return handleExceptionInternal(ex, errors,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value
            = { Exception.class })
    protected ResponseEntity<Object> handleAnyException(
            RuntimeException ex, WebRequest request) {
        logger.error("There is an error in the application "+ ex);
        ex.printStackTrace();
        ArrayList errors=new ArrayList();
        Error error=new Error();
        error.setErrorCode(ErrorConstants.ERR_100_CD);
        error.setErrrorDescription(ex.getMessage());
        errors.add(error);
        return handleExceptionInternal(ex, errors,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}