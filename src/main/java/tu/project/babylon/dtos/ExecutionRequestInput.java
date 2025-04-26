package tu.project.babylon.dtos;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class ExecutionRequestInput {

    private String name;
    private String executor;
}
