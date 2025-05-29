package tu.project.babylon.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import tu.project.babylon.errors.ExecutorRequestException;
import tu.project.babylon.models.Execution;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public final class ExecutionRequestConverter {

    private ExecutionRequestConverter() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class");
    }

    public static Execution mapInputToExecutionRequest(ExecutionInput input) {
        try {
            ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
            System.out.println("INPUT: " + input);
            Map<String, Object> executorMap = yamlReader.readValue(input.getExecutorValue(), Map.class);

            Execution execution = new Execution();
            execution.setName(input.getName());
            execution.setExecutor(executorMap);
            execution.setCreated(LocalDateTime.now());
            return execution;
        } catch (IOException e) {
            throw new ExecutorRequestException("Invalid YAML provided in executor field", e);
        }
    }
}
