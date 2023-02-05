package com.greenrollsms.common.exception;

import com.greenrollsms.common.Message;

public class CustomException extends Exception {

    protected Message[] messages;

    public CustomException() {
        super();

    }


    public CustomException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CustomException(Message...message) {
        this.messages = message;
    }

    public Message[] getMessages() {
        return this.messages;
    }

}

