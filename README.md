# Dispatch Load Balancer

A Spring Boot application that optimizes the allocation of delivery orders to a fleet of vehicles based on their locations, capacity, and priority. This system minimizes total travel distance using a greedy strategy and the Haversine formula.

## Features
-   **Intelligent Dispatching**:
    -   **Priority-First**: High-priority orders are always considered first for assignment.
    -   **Distance Minimization**: Uses the **Haversine formula** to find the nearest available vehicle.
    -   **Capacity Management**: Strictly enforces vehicle capacity constraints.
    -   **Unassigned Order Handling**: Gracefully handles and reports orders that cannot be assigned due to capacity limits.
-   **Robust Architecture**:
    -   **Type-Safe Enums**: Uses `Priority` (HIGH, MEDIUM, LOW) for strict validation.
    -   **Strategy Pattern**: Dispatch logic is encapsulated, allowing easy swapping of algorithms.
    -   **DTO Pattern**: Clear separation between API contracts and internal domain entities.
-   **Error Handling**: Global exception handling for validation errors and malformed requests.

## Technology Stack
-   **Java 17+**
-   **Spring Boot 3.3.2**
-   **Spring Data JPA** & **H2 Database** (In-Memory)
-   **JUnit 5**, **Mockito**, **MockMvc**

## Algorithm Logic
The application uses a **Greedy Priority-Based Strategy**:
1.  **Sort Orders**: Orders are sorted primarily by Priority (HIGH > MEDIUM > LOW) and secondarily by weight.
2.  **Find Best Vehicle**: For each order, the system iterates through all vehicles.
3.  **Constraints**: Checks if the vehicle has enough remaining capacity.
4.  **Optimization**: Calculates the Haversine distance from the vehicle's current location to the order's location.
5.  **Assignment**: The order is assigned to the nearest valid vehicle. If no vehicle can fit the order, it is added to the `unassignedOrders` list.

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
```json
{
  "dispatchPlan": [
    {
      "vehicleId": "VEH001",
      "totalLoad": 10,
      "totalDistance": "5.2 km",
      "assignedOrders": [...]
    }
  ],
  "unassignedOrders": []
}
```

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

The project includes a comprehensive test suite with **100% pass rate** (13 tests):

1.  **Unit Tests**: Verify the `PriorityDistanceStrategy` logic, including edge cases like:
    -   Zero-distance orders.
    -   Exact capacity matches.
    -   Mixed priority handling.
2.  **Integration Tests**:
    -   **Assignment Dataset Verification**: A specific test (`DispatchIntegrationTest`) runs the **full 30-order dataset** provided in the assignment against the 5-vehicle fleet. It verifies that all orders are correctly processed and constraints are respected.

To run the full suite:
```bash
./mvnw test
```
*Check the console output during testing to see the detailed dispatch plan for the assignment dataset.*
