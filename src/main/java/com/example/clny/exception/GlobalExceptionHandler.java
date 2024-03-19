package com.example.clny.exception;

import com.example.clny.exception.custom.EmailAlreadyInUseException;
import com.example.clny.exception.custom.EmptyFileException;
import com.example.clny.exception.custom.NoAccountAssociatedWithEmailException;
import com.example.clny.exception.custom.WrongPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyInUseException() {
        ErrorResponse errorResponse = new ErrorResponse("Email already in use. Please login instead.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoAccountAssociatedWithEmailException.class)
    public ResponseEntity<ErrorResponse> handleNoAccountAssociatedWithEmailException() {
        ErrorResponse errorResponse = new ErrorResponse("There is no account associated with this email. Please register instead.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ErrorResponse> handleWrongPasswordException() {
        ErrorResponse errorResponse = new ErrorResponse("Wrong password. Please try again.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<ErrorResponse> handleEmptyFileException() {
        ErrorResponse errorResponse = new ErrorResponse("No file provided. Please provide a file.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
