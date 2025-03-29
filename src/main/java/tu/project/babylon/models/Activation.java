package tu.project.babylon.models;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Activation {
    private UUID executionId;
}
