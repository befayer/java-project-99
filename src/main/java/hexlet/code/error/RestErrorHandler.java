package hexlet.code.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles the exception when a username is not found.
     *
     * @param ex The exception to handle.
     * @return ResponseEntity with the error message and HTTP status code UNAUTHORIZED.
     */
    @ExceptionHandler(value = {
        UsernameNotFoundException.class,
        NoSuchElementException.class,
        BadCredentialsException.class
    })
    public ResponseEntity<?> handleUsernameNotFoundException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles the exception when access is denied.
     *
     * @param ex The exception to handle.
     * @return ResponseEntity with the error message and HTTP status code FORBIDDEN.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    /**
     * Handles the exception when a SQL integrity constraint violation occurs.
     *
     * @param ex The exception to handle.
     * @return ResponseEntity with the error message and HTTP status code BAD_REQUEST.
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleSQLIntegrityConstraintViolationException(SQLException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
