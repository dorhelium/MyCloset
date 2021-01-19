package TrackingApp.Exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApplicationError {
    private int code;
    private String message;
    private HttpStatus status;
}
