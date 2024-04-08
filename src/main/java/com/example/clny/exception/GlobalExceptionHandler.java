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

    @ExceptionHandler(AlreadyFollowingException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyFollowingException() {
        ErrorResponse errorResponse = new ErrorResponse("You are already following this user.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFollowingException.class)
    public ResponseEntity<ErrorResponse> handleNotFollowingException() {
        ErrorResponse errorResponse = new ErrorResponse("You are not following this user.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyPostException.class)
    public ResponseEntity<ErrorResponse> handleEmptyPostException() {
        ErrorResponse errorResponse = new ErrorResponse("Content section of the post cannot be empty.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostTooLongException.class)
    public ResponseEntity<ErrorResponse> handlePostTooLongException() {
        ErrorResponse errorResponse = new ErrorResponse("Content length cannot exceed 250 characters.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoPostAuthorException.class)
    public ResponseEntity<ErrorResponse> handleNoPostAuthorException() {
        ErrorResponse errorResponse = new ErrorResponse("Post must have an author.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyCommentException.class)
    public ResponseEntity<ErrorResponse> handleEmptyCommentException() {
        ErrorResponse errorResponse = new ErrorResponse("Content section of the comment cannot be empty.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommentTooLongException.class)
    public ResponseEntity<ErrorResponse> handleCommentTooLongException() {
        ErrorResponse errorResponse = new ErrorResponse("Content length cannot exceed 250 characters.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoCommentAuthorException.class)
    public ResponseEntity<ErrorResponse> handleNoCommentAuthorException() {
        ErrorResponse errorResponse = new ErrorResponse("Comment must have an author.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoCommentPostException.class)
    public ResponseEntity<ErrorResponse> handleNoCommentPostException() {
        ErrorResponse errorResponse = new ErrorResponse("Comment must be linked to a post.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
