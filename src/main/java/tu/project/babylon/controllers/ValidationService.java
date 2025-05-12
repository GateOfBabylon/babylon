package tu.project.babylon.controllers;

import org.springframework.stereotype.Service;
import tu.project.babylon.dtos.ExecutionRequestInput;
import tu.project.babylon.errors.ExecutorFailedException;

@Service
public class ValidationService {

    public void validateRequest(ExecutionRequestInput request) {
        if (request.getName() == null) {
            throw new ExecutorFailedException("Defined 'name' in the request was null");
        }
    }
}
