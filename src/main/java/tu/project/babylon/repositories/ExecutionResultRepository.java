package tu.project.babylon.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tu.project.babylon.models.ExecutionResult;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExecutionResultRepository extends MongoRepository<ExecutionResult, UUID> {
    List<ExecutionResult> findAllByExecutorId(UUID executorId);
}