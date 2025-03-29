package tu.project.babylon.errors;

import lombok.Getter;

import java.io.Serial;
import java.util.List;

@Getter
public class ExecutorFailedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    private final List<String> executorOutput;

    public ExecutorFailedException(String message, String executorOutput) {
        super(message);
        this.executorOutput = this.setExecutorOutput(executorOutput);
    }

    private List<String> setExecutorOutput(String executorOutput) {
        return List.of(executorOutput.split("\n"));
    }

}
