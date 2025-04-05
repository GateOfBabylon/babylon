package tu.project.babylon.errors;

import lombok.Getter;

import java.io.Serial;

@Getter
public class ExecutionNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ExecutionNotFoundException(String message) {
        super(message);
    }
}
