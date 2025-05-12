package tu.project.babylon.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import tu.project.babylon.errors.ExecutorRequestException;
import tu.project.babylon.models.ExecutionRequest;

import java.io.IOException;
import java.util.Map;

public final class ExecutionRequestConverter {

    private ExecutionRequestConverter() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class");
    }

    public static ExecutionRequest mapInputToExecutionRequest(ExecutionRequestInput input) {
        try {
            ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
            Map<String, Object> executorMap = yamlReader.readValue(input.getExecutor(), Map.class);

            ExecutionRequest executionRequest = new ExecutionRequest();
            executionRequest.setName(input.getName());
            executionRequest.setExecutor(executorMap);
            return executionRequest;
        } catch (IOException e) {
            throw new ExecutorRequestException("Invalid YAML provided in executor field", e);
        }
    }
}
