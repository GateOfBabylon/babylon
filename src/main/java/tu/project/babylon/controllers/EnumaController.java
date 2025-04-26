package tu.project.babylon.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tu.project.babylon.dtos.ExecutionRequestConverter;
import tu.project.babylon.dtos.ExecutionRequestInput;
import tu.project.babylon.errors.ExecutorRequestException;
import tu.project.babylon.models.ExecutionRequest;
import tu.project.babylon.models.Activation;
import tu.project.babylon.models.ExecutionResult;
import tu.project.babylon.services.EnumaService;


import java.io.IOException;
import java.util.Map;
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

    @PostMapping("/save")
    @Transactional
    public ResponseEntity<Void> saveEnumaScript(@RequestBody ExecutionRequestInput requestInput) {
        validationService.validateRequest(requestInput);
        log.info("Received request to save Enuma Executor: {}", requestInput.getName());

        ExecutionRequest request = ExecutionRequestConverter.mapInputToExecutionRequest(requestInput);
        enumaService.saveExecutor(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/run")
    @Transactional
    public ResponseEntity<Activation> runEnumaScript(@RequestBody ExecutionRequestInput requestInput) {
        validationService.validateRequest(requestInput);
        log.info("Received async request to run Enuma script: {}", requestInput.getName());

        ExecutionRequest request = enumaService.getExecutionRequest(requestInput.getName());
        UUID id = enumaService.executeAsync(request);
        return ResponseEntity.accepted().body(
                Activation.builder()
                        .executionId(id)
                        .build()
        );
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<ExecutionResult> getResult(@PathVariable UUID id) {
        ExecutionResult result = enumaService.getResult(id);
        return ResponseEntity.ok(result);
    }

}
