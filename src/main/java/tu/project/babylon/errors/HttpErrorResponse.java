package tu.project.babylon.errors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder(builderClassName = "HttpErrorResponseBuilder", toBuilder = true)
public class HttpErrorResponse {

    private String message;
    private LocalDateTime timestamp;
    private HttpStatus status;
    private JsonNode details;

    public static class HttpErrorResponseBuilder {

        private static final ObjectMapper MAPPER = new ObjectMapper();

        public HttpErrorResponseBuilder now() {
            this.timestamp = LocalDateTime.now();
            return this;
        }

        public HttpErrorResponseBuilder details(@NonNull Object details) {
            this.details = MAPPER.valueToTree(details);
            return this;
        }
    }
}
