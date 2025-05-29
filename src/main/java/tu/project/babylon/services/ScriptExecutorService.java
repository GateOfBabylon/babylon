package tu.project.babylon.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tu.project.babylon.errors.ExecutionNotFoundException;
import tu.project.babylon.models.ExecutionResult;
import tu.project.babylon.models.ExecutionStatus;
import tu.project.babylon.repositories.ExecutionResultRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.lang.String.format;

@Slf4j
@Service
public class ScriptExecutorService {

    private final ScriptProcessFactory processFactory;
    private final ExecutionResultRepository repository;

    public ScriptExecutorService(ScriptProcessFactory processFactory, ExecutionResultRepository repository) {
        this.processFactory = processFactory;
        this.repository = repository;
    }

    @Async
    public void run(UUID id, String scriptPath) {
        log.info("Async execution started for ID: {}", id);

        try {
            ExecutionResult running = getExecutionResult(id);
            running.setStatus(ExecutionStatus.RUNNING);
            repository.save(running);

            Process process = processFactory.create(scriptPath);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    running.addToOutput(line);
                    repository.save(running);
                }

                while ((line = errorReader.readLine()) != null) {
                    running.addToOutput(line);
                    repository.save(running);
                }

                int exitCode = process.waitFor();
                ExecutionStatus status = (exitCode == 0) ? ExecutionStatus.SUCCESS : ExecutionStatus.ERROR;
                running.setStatus(status);
            } catch (InterruptedException e) {
                Thread.currentThread()
                        .interrupt();
                updateExecutorWithError(id, e);
            }
            repository.save(running);

        } catch (IOException e) {
            updateExecutorWithError(id, e);
        } finally {
            ExecutionResult result = getExecutionResult(id);
            result.setTimestamp(LocalDateTime.now());
            repository.save(result);
        }
    }

    private void updateExecutorWithError(UUID id, Exception e) {
        String message = format("Execution failed for ID: %s", id);
        log.error(message, e);
        ExecutionResult failed = getExecutionResult(id);
        failed.setStatus(ExecutionStatus.ERROR);
        failed.addToOutput(format("ERROR: %s", e.getMessage()));
        failed.setTimestamp(LocalDateTime.now());
        repository.save(failed);
    }

    private ExecutionResult getExecutionResult(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ExecutionNotFoundException("ExecutionResult not found for ID: " + id));
    }

}

