package com.example.clny.exception;

import com.example.clny.exception.custom.*;
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

    @ExceptionHandler(FileNotImageException.class)
    public ResponseEntity<ErrorResponse> handleFileNotImageException() {
        ErrorResponse errorResponse = new ErrorResponse("Provided file is not a valid image.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileTooLargeException.class)
    public ResponseEntity<ErrorResponse> handleFileTooLargeException() {
        ErrorResponse errorResponse = new ErrorResponse("Provided file is too large. Max image size is 10MB.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyBiographyException.class)
    public ResponseEntity<ErrorResponse> handleEmptyBiographyException() {
        ErrorResponse errorResponse = new ErrorResponse("Biography section cannot be empty.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BiographyTooLongException.class)
    public ResponseEntity<ErrorResponse> handleBiographyTooLongException() {
        ErrorResponse errorResponse = new ErrorResponse("Biography length cannot exceed 150 characters.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptySearchStringException.class)
    public ResponseEntity<ErrorResponse> handleEmptySearchStringException() {
        ErrorResponse errorResponse = new ErrorResponse("Search string cannot be empty.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
