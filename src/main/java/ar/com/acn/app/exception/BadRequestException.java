package ar.com.acn.app.exception;

public class BadRequestException extends Exception{

    public BadRequestException(String message) {
        super(message);
    }
}
