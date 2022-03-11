package com.code.challenge.exception;

import com.code.challenge.dto.rest_response.RestResponse;
import com.code.challenge.dto.rest_response.RestResponseType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {UserNotFoundException.class})
    protected ResponseEntity<RestResponse> handleNotFoundException(UserNotFoundException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestResponse(RestResponseType.ERROR, "User Not Found!"));
    }
//
//    @org.springframework.web.bind.annotation.ExceptionHandler(value = {MankalaOutOfBandException.class})
//    protected ResponseEntity<RestResponse> handleOutOfBandException(MankalaOutOfBandException ex, WebRequest request) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestResponse(RestResponseType.ERROR, ex.getMessage()));
//    }
//
//    @org.springframework.web.bind.annotation.ExceptionHandler(value = {MankalaIsTheWrongTurnException.class})
//    protected ResponseEntity<RestResponse> handleMakeTheWrongTurn(MankalaIsTheWrongTurnException ex, WebRequest request) {
//        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new RestResponse(RestResponseType.WARNING, ex.getMessage()));
//    }
//
//    @org.springframework.web.bind.annotation.ExceptionHandler(value = {MankalaWebException.class})
//    protected ResponseEntity<RestResponse> handleWebException(RuntimeException ex, WebRequest request) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestResponse(RestResponseType.WARNING, ex.getMessage()));
//    }
}
