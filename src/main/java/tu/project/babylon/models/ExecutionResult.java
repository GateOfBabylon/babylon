package tu.project.babylon.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Document(collection = "executions")
public class ExecutionResult {

    @Id
    private UUID id;
    private UUID executorId;
    private String scriptPath;
    private ExecutionStatus status;
    private List<String> output = new ArrayList<>();

    public ExecutionResult(String scriptPath) {
        this.id = UUID.randomUUID();
        this.scriptPath = scriptPath;
        this.status = ExecutionStatus.PENDING;
    }

    public void addToOutput(String log) {
        output.add(log);
    }
}