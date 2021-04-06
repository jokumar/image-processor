package com.geeks18.exceptions;

public class MessageCommunicationException  extends ServiceException {
    private String id;
    private String desc;


    public MessageCommunicationException( String errorcode, String errorMessage) {
        super(errorcode,errorMessage);
        this.id = errorcode;
        this.desc = errorMessage;

    }
}
