package tu.project.babylon.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tu.project.babylon.models.ExecutionRequest;

import java.util.Optional;

public interface ExecutionRequestRepository extends MongoRepository<ExecutionRequest, String> {

    Optional<ExecutionRequest> findByName(String name);
}
