package tu.project.babylon.services;

import org.springframework.stereotype.Service;
import tu.project.babylon.dtos.ExecutionInput;
import tu.project.babylon.errors.ExecutorFailedException;

@Service
public class ValidationService {

    public void validateRequest(ExecutionInput request) {
        if (request.getName() == null) {
            throw new ExecutorFailedException("Defined 'name' in the request was null");
        }
    }
}
