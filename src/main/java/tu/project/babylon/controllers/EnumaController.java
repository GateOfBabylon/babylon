package tu.project.babylon.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tu.project.babylon.dtos.ExecutionRequest;
import tu.project.babylon.models.Activation;
import tu.project.babylon.models.ExecutionResult;
import tu.project.babylon.services.EnumaService;

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

    @PostMapping("/run")
    @Transactional
    public ResponseEntity<Activation> runEnumaScript(@RequestBody ExecutionRequest request) {
        validationService.validateRequest(request);
        log.info("Received async request to run Enuma script: {}", request);
        UUID id = enumaService.executeAsync(request.getScriptPath());
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
