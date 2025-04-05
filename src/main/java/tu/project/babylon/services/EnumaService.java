package tu.project.babylon.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tu.project.babylon.errors.ExecutionNotFoundException;
import tu.project.babylon.models.ExecutionResult;
import tu.project.babylon.repositories.ExecutionResultRepository;

import java.util.UUID;

import static java.lang.String.format;

@Service
public class EnumaService {

    private final ExecutionResultRepository repository;
    private final ScriptExecutorService executor;

    public EnumaService(ExecutionResultRepository repository, ScriptExecutorService executor) {
        this.repository = repository;
        this.executor = executor;
    }

    @Transactional
    public UUID executeAsync(String scriptPath) {
        ExecutionResult result = new ExecutionResult(scriptPath);
        result = repository.save(result);
        executor.run(result.getId(), scriptPath);
        return result.getId();
    }

    public ExecutionResult getResult(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ExecutionNotFoundException(format("Execution with id '%s' is not found", id)));
    }
}
