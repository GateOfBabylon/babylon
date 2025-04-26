package tu.project.babylon.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tu.project.babylon.models.ExecutionRequest;
import tu.project.babylon.errors.ExecutionNotFoundException;
import tu.project.babylon.errors.ExecutorRequestException;
import tu.project.babylon.models.ExecutionResult;
import tu.project.babylon.repositories.ExecutionRequestRepository;
import tu.project.babylon.repositories.ExecutionResultRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import static java.lang.String.format;

@Slf4j
@Service
public class EnumaService {

    private final ExecutionResultRepository resultRepository;
    private final ExecutionRequestRepository requestRepository;
    private final ScriptExecutorService executor;

    public EnumaService(ExecutionResultRepository resultRepository, ExecutionRequestRepository requestRepository, ScriptExecutorService executor) {
        this.resultRepository = resultRepository;
        this.requestRepository = requestRepository;
        this.executor = executor;
    }

    @Transactional
    public UUID executeAsync(ExecutionRequest request) {
        requestRepository.save(request);

        try {
            Path tempFile = Files.createTempFile(String.format("executor-%s-", request.getName()), ".ea");
            JsonNode executorObj = new ObjectMapper().convertValue(request.getExecutor(), JsonNode.class);
            Files.writeString(tempFile, executorObj.toPrettyString(), StandardOpenOption.TRUNCATE_EXISTING);

            ExecutionResult result = new ExecutionResult(tempFile.toString());
            result.setId(request.getId());
            resultRepository.save(result);

            executor.run(result.getId(), tempFile.toString());

            return result.getId();
        } catch (IOException e) {
            throw new ExecutorRequestException("Failed to create temp file for execution", e);
        }
    }


    public ExecutionResult getResult(UUID id) {
        return resultRepository.findById(id).orElseThrow(() -> new ExecutionNotFoundException(format("Execution with id '%s' is not found", id)));
    }

    public void saveExecutor(ExecutionRequest request) {
        requestRepository.save(request);
    }

    public ExecutionRequest getExecutionRequest(String name) {
        return requestRepository.findByName(name)
                .orElseThrow(() -> new ExecutionNotFoundException(format("Execution request with name '%s' is not found", name)));
    }
}
