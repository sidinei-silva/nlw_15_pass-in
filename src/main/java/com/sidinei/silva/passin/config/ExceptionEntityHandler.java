package com.sidinei.silva.passin.config;

import com.sidinei.silva.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import com.sidinei.silva.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import com.sidinei.silva.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import com.sidinei.silva.passin.domain.event.exceptions.EventFullException;
import com.sidinei.silva.passin.domain.event.exceptions.EventNotFoundException;
import com.sidinei.silva.passin.dto.general.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionEntityHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity handleEventNotFound(EventNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AttendeeNotFoundException.class)
    public ResponseEntity handleAttendeeNotFound(AttendeeNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AttendeeAlreadyExistException.class)
    public ResponseEntity handleAttendeeAlreadyExists(AttendeeAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(CheckInAlreadyExistsException.class)
    public ResponseEntity handleCheckInAlreadyExists(CheckInAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(EventFullException.class)
    public ResponseEntity<ErrorResponseDTO> handleEventFull(EventFullException e) {
        ErrorResponseDTO erroResponseDTO = new ErrorResponseDTO("Event is full");
        return ResponseEntity.badRequest().body(erroResponseDTO);
    }
}
