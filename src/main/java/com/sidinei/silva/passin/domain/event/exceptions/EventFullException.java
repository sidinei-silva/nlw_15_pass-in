package com.sidinei.silva.passin.domain.event.exceptions;

public class EventFullException extends RuntimeException{
    public EventFullException(String message) {
        super(message);
    }
}
