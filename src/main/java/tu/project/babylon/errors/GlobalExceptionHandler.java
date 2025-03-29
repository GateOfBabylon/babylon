package tu.project.babylon.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExecutorFailedException.class)
    public ResponseEntity<HttpErrorResponse> handleExecutorFailedException(ExecutorFailedException ex) {
        HttpErrorResponse errorResponse = HttpErrorResponse.builder()
                .message(ex.getMessage())
                .now()
                .status(HttpStatus.BAD_REQUEST)
                .details(ex.getExecutorOutput())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
