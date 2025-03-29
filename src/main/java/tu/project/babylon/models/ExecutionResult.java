package tu.project.babylon.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@NoArgsConstructor
@Document(collection = "executions")
public class ExecutionResult {

    @Id
    private UUID id;
    private String scriptPath;
    private ExecutionStatus status;
    private String output;

    public ExecutionResult(String scriptPath) {
        this.id = UUID.randomUUID();
        this.scriptPath = scriptPath;
        this.status = ExecutionStatus.PENDING;
    }
}