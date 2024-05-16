package ru.arman.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.arman.dto.ErrorMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler( {SignatureException.class, MalformedJwtException.class, ExpiredJwtException.class, UnsupportedJwtException.class, IllegalArgumentException.class} )
    public ResponseEntity<ErrorMessage> handleSignatureException(RuntimeException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
