Here's a well-structured `README.md` for the **GateOfBabylon** repository based on the source code and the `application.yaml` configuration:

---

# GateOfBabylon

**GateOfBabylon** is a lightweight Java Spring Boot service for managing and executing dynamic code "activations" (scripts) and tracking their outcomes. It exposes HTTP endpoints to run code, stores execution results in MongoDB, and provides status querying functionality.

---

##  Features

-  **Execute Activations** (scripts or logic units) via HTTP POST
- ️ **Track Execution Results** (status, output, errors)
-  **Query Status** of activations by ID
- ️ **Script Execution Logic** isolated in a service
-  **MongoDB Persistence** for execution records
-  **REST API** exposed via Spring Web

---

##  Example Activation Flow

1. **POST** an activation (with script code and optional params).
2. Service executes it and stores:
    - Status (`PENDING`, `RUNNING`, `COMPLETED`, `FAILED`)
    - Output
    - Timestamp
3. Query the status or results via GET endpoint.

---

##  Running the Project

### Prerequisites

- Java 17+
- Maven
- MongoDB running locally on port `27017`

### Start MongoDB

```bash
docker run --rm -d -p 27017:27017 --name mongodb mongo
```

### Build and Run

```bash
mvn clean install
mvn spring-boot:run
```

By default, the app runs on: [http://localhost:1337](http://localhost:1337)

---

##  API Endpoints

### `POST /enuma/execute`
Execute a new activation script.

**Request Body**
```json
{
  "scriptPath": "<path-to-ea-file>"
}
```

**Response**
```json
{
  "executionId": "<uuid>"
}
```

---

### `GET /enuma/status/{executionId}`
Retrieve the execution result by ID.

**Response**
```json
{
  "executionId": "<uuid>",
  "status": "<predefined-status>",
  "output": ["<lis-of-logs>"]
}
```

---

## ⚙️ Configuration

### `application.yaml`

```yaml
spring:
  application:
    name: GateOfBabylon
  data:
    mongodb:
      uri: mongodb://localhost:27017/gateofbabylon

server:
  port: 1337
```