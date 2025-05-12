package tu.project.babylon.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.UUID;

@Data
@Document(collection = "execution_requests")
@NoArgsConstructor
public class  ExecutionRequest {

    @Id
    private UUID id = UUID.randomUUID();
    @Indexed(unique = true)
    private String name;
    private Map<String, Object> executor;


}