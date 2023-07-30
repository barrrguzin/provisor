package ru.ptkom.provisor.exception;

public class PBXUserNotFoundException extends RuntimeException{


    public PBXUserNotFoundException() {
    }

    public PBXUserNotFoundException(String message) {
        super(message);
    }

    public PBXUserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PBXUserNotFoundException(Throwable cause) {
        super(cause);
    }

    public PBXUserNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
