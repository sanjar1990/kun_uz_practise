package com.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
@ExceptionHandler(AppBadRequestException.class)
public ResponseEntity<String>handler(AppBadRequestException e) {
return ResponseEntity.badRequest().body(e.getMessage());
}
@ExceptionHandler(ItemAlreadyExistsException.class)
    public ResponseEntity<String>handler(ItemAlreadyExistsException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
}
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<String>handler(ItemNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(MethodNotAllowed.class)
    public ResponseEntity<String>handler(MethodNotAllowed e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<String>handler(UnAuthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
    @ExceptionHandler(AppForbiddenException.class)
    public ResponseEntity<String>handler(AppForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String>handler(RuntimeException e) {
    e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String>handler(AccessDeniedException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
