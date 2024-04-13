package org.codecrafterslab.unity.exception.api.impl;

import org.codecrafterslab.unity.exception.api.Status;

public class DefaultStatus implements Status {
    private final int code;
    private final int httpStatus;
    private String message;

    public DefaultStatus(int code, String message, int httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Status message(String message) {
        this.message = message;
        return this;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }
}
