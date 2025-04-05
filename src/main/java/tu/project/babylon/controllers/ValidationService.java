package tu.project.babylon.controllers;

import org.springframework.stereotype.Service;
import tu.project.babylon.dtos.ExecutionRequest;
import tu.project.babylon.errors.ExecutorFailedException;

@Service
public class ValidationService {

    public void validateRequest(ExecutionRequest request) {
        if (request.getScriptPath() == null){
            throw new ExecutorFailedException("Defined 'scriptPath' in the request was null");
        }
    }
}
