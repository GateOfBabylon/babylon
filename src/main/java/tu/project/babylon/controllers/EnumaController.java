package tu.project.babylon.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tu.project.babylon.dtos.ExecutionRequestConverter;
import tu.project.babylon.dtos.ExecutionInput;
import tu.project.babylon.models.Execution;
import tu.project.babylon.models.Activation;
import tu.project.babylon.models.ExecutionResult;
import tu.project.babylon.services.EnumaService;
import tu.project.babylon.services.ValidationService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/enuma")
public class EnumaController {

    private final EnumaService enumaService;
    private final ValidationService validationService;

    public EnumaController(EnumaService enumaService, ValidationService validationService) {
        this.enumaService = enumaService;
        this.validationService = validationService;
    }

    @Transactional
    @PostMapping("/save")
    public ResponseEntity<Void> saveEnumaScript(@RequestBody ExecutionInput requestInput) {
        validationService.validateRequest(requestInput);
        log.info("Received request to save Enuma Executor: {}", requestInput.getName());

        Execution request = ExecutionRequestConverter.mapInputToExecutionRequest(requestInput);
        enumaService.saveExecutor(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Transactional
    @PostMapping("/run")
    public ResponseEntity<Activation> runEnumaScript(@RequestBody ExecutionInput requestInput) {
        validationService.validateRequest(requestInput);
        log.info("Received async request to run Enuma script: {}", requestInput.getName());

        Execution request = enumaService.getExecutionRequest(requestInput.getName());
        UUID id = enumaService.executeAsync(request);
        return ResponseEntity.accepted()
                .body(Activation.builder()
                        .executionId(id)
                        .build());
    }

    @GetMapping("/executors")
    public ResponseEntity<List<Execution>> getAllExecutors() {
        List<Execution> executors = enumaService.getAllExecutors();
        return ResponseEntity.ok(executors);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<ExecutionResult> getExecutionResultById(@PathVariable UUID id) {
        ExecutionResult result = enumaService.getResult(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/status/list/{executorName}")
    public ResponseEntity<List<ExecutionResult>> getResultsForExecutor(@PathVariable String executorName) {
        List<ExecutionResult> result = enumaService.getResultsForExecutor(executorName);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteExecutor(@PathVariable UUID id) {
        enumaService.deleteExecutor(id);
        return ResponseEntity.noContent()
                .build();
    }

}
