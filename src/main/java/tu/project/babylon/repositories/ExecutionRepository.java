package tu.project.babylon.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tu.project.babylon.models.Execution;

import java.util.Optional;
import java.util.UUID;

public interface ExecutionRepository extends MongoRepository<Execution, UUID> {

    Optional<Execution> findByName(String name);
}
