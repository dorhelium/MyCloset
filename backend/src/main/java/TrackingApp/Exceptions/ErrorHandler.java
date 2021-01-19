package TrackingApp.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ApplicationError> handleInvalidDataException
            (InvalidDataException ex, WebRequest webRequest) {
        ApplicationError error = new ApplicationError();
        error.setCode(404);
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (UnauthorizedActionException.class)
    public ResponseEntity<ApplicationError> handleInvalidDataException
            (UnauthorizedActionException ex, WebRequest webRequest) {
        ApplicationError error = new ApplicationError();
        error.setCode(403);
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }


}