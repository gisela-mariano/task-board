package br.com.exception;

public class FinishedCardException extends RuntimeException {
    public FinishedCardException(String message) {
        super(message);
    }
}
