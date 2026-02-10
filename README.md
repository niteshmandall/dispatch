# Dispatch Load Balancer

A Spring Boot application that optimizes the allocation of delivery orders to a fleet of vehicles based on their locations, capacity, and priority. This system minimizes total travel distance using a greedy strategy and the Haversine formula.

## Features

-   **Order Management**: Ingest delivery orders with priority (HIGH, MEDIUM, LOW).
-   **Vehicle Management**: Track fleet status, location, and capacity.
-   **Intelligent Dispatching**:
    -   Assigns orders based on **Priority first**.
    -   Minimizes travel distance using the **Haversine formula**.
    -   Ensures vehicle capacity is not exceeded.
-   **Clean Architecture**: Follows Controller-Service-Repository pattern with Strategy Pattern for dispatch logic.

## Technology Stack

-   **Java 17+**
-   **Spring Boot 3.x**
-   **H2 Database** (In-Memory for simplicity)
-   **Spring Data JPA**
-   **JUnit 5 & Mockito** (Testing)

## API Endpoints

### 1. Add Orders
**POST** `/api/dispatch/orders`
```json
{
  "orders": [
    {
      "orderId": "ORD001",
      "latitude": 12.9716,
      "longitude": 77.5946,
      "address": "MG Road, Bangalore",
      "packageWeight": 10,
      "priority": "HIGH"
    }
  ]
}
```

### 2. Add Vehicles
**POST** `/api/dispatch/vehicles`
```json
{
  "vehicles": [
    {
      "vehicleId": "VEH001",
      "capacity": 100,
      "currentLatitude": 12.9716,
      "currentLongitude": 77.6413,
      "currentAddress": "Indiranagar, Bangalore"
    }
  ]
}
```

### 3. Get Dispatch Plan
**GET** `/api/dispatch/plan`
Response includes the optimized assignment of orders to vehicles.

## How to Run

1.  **Build the project:**
    ```bash
    ./mvnw clean install
    ```
2.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```
3.  **Access the API:**
    The application runs on `http://localhost:8080`.

## Testing

The project includes comprehensive Unit and Integration tests.
To run tests:
```bash
./mvnw test
```

## Architecture & Design Decisions

-   **Strategy Pattern:** The dispatch logic is encapsulated in `DispatchOptimizationStrategy`. The current implementation (`PriorityDistanceStrategy`) uses a greedy approach. This allows future extension to more complex algorithms (e.g., Genetic Algorithms) without modifying the core service.
-   **DTO Pattern:** Strict separation between internal Entities and external API DTOs.
-   **Exception Handling:** Global exception handler ensures consistent error responses.
