package tu.project.babylon.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tu.project.babylon.models.Execution;
import tu.project.babylon.errors.ExecutionNotFoundException;
import tu.project.babylon.errors.ExecutorRequestException;
import tu.project.babylon.models.ExecutionResult;
import tu.project.babylon.repositories.ExecutionRepository;
import tu.project.babylon.repositories.ExecutionResultRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Slf4j
@Service
public class EnumaService {

    private final ExecutionResultRepository resultRepository;
    private final ExecutionRepository executionRepository;
    private final ScriptExecutorService executor;

    public EnumaService(ExecutionResultRepository resultRepository, ExecutionRepository requestRepository,
                        ScriptExecutorService executor) {
        this.resultRepository = resultRepository;
        this.executionRepository = requestRepository;
        this.executor = executor;
    }

    @Transactional
    public UUID executeAsync(Execution request) {
        executionRepository.save(request);

        try {
            Path tempFile = Files.createTempFile(String.format("executor-%s-", request.getName()), ".ea");
            JsonNode executorObj = new ObjectMapper().convertValue(request.getExecutor(), JsonNode.class);
            Files.writeString(tempFile, executorObj.toPrettyString(), StandardOpenOption.TRUNCATE_EXISTING);

            ExecutionResult result = new ExecutionResult(tempFile.toString());
            result.setId(UUID.randomUUID());
            result.setExecutorId(request.getId());
            resultRepository.save(result);

            executor.run(result.getId(), tempFile.toString());

            return result.getId();
        } catch (IOException e) {
            throw new ExecutorRequestException("Failed to create temp file for execution", e);
        }
    }

    public ExecutionResult getResult(UUID id) {
        return resultRepository.findById(id)
                .orElseThrow(() -> new ExecutionNotFoundException(format("Execution with id '%s' is not found", id)));
    }

    public List<ExecutionResult> getResultsForExecutor(String executorName) {
        Execution request = executionRepository.findByName(executorName)
                .orElseThrow(() -> new ExecutionNotFoundException(
                        String.format("Executor with name '%s' not found", executorName)));

        return resultRepository.findAllByExecutorId(request.getId());
    }

    public void saveExecutor(Execution request) {
        executionRepository.save(request);
    }

    public Execution getExecutionRequest(String name) {
        return executionRepository.findByName(name)
                .orElseThrow(() -> new ExecutionNotFoundException(
                        format("Execution request with name '%s' is not found", name)));
    }

    public List<Execution> getAllExecutors() {
        return executionRepository.findAll();
    }

    public void deleteExecutor(UUID id) {
        executionRepository.deleteById(id);
    }

}
