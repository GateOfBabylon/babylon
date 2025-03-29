package tu.project.babylon.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tu.project.babylon.models.ExecutionResult;
import tu.project.babylon.models.ExecutionStatus;
import tu.project.babylon.repositories.ExecutionResultRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ScriptExecutorService {

    private final ExecutionResultRepository repository;

    public ScriptExecutorService(ExecutionResultRepository repository) {
        this.repository = repository;
    }

    @Async
    public void run(UUID id, String scriptPath) {
        log.info("Async execution started for ID: {}", id);

        try {
            ExecutionResult running = repository.findById(id).orElseThrow();
            running.setStatus(ExecutionStatus.RUNNING);
            repository.save(running);

            ProcessBuilder builder = new ProcessBuilder("enumago", scriptPath);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            String output;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                output = reader.lines().collect(Collectors.joining("\n"));
            }

            int exitCode = process.waitFor();
            ExecutionStatus status = (exitCode == 0) ? ExecutionStatus.SUCCESS : ExecutionStatus.ERROR;

            running.setStatus(status);
            running.setOutput(output);
            repository.save(running);

        } catch (Exception e) {
            log.error("Execution failed for ID: {}", id, e);
            ExecutionResult failed = repository.findById(id).orElseThrow();
            failed.setStatus(ExecutionStatus.ERROR);
            failed.setOutput("Error: " + e.getMessage());
            repository.save(failed);
        }
    }
}

