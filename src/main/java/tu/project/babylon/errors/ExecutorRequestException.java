package tu.project.babylon.errors;

import lombok.Getter;

import java.io.Serial;

@Getter
public class ExecutorRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ExecutorRequestException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
