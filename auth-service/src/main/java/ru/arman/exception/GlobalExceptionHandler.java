package ru.arman.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.arman.dto.ErrorMessage;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({UserNotFoundException.class, UserAlreadyExistsException.class})
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(RuntimeException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
}
