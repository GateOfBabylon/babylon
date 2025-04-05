package tu.project.babylon.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ScriptProcessFactory {

    @Value("${self.executor.command:enumago}")
    private String executorCommand;

    public Process create(String scriptPath) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(executorCommand, scriptPath);
        builder.redirectErrorStream(true);
        return builder.start();
    }
}
