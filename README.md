# Car Rental Project

A robust car rental system built with Spring Boot implementing MVC architecture and factory patterns for vehicle management.

## Architecture Overview

### MVC Pattern

- **Model**: Entity classes (Car, CarType, User, Reservation)
- **View**: Vaadin UI components for web interface
- **Controller**: REST endpoints handling business logic routing

### Design Patterns

- **Factory Pattern**: Vehicle creation and type management
- **Repository Pattern**: Data access abstraction
- **DTO Pattern**: Clean data transfer between layers
- **Service Layer Pattern**: Business logic encapsulation

## Project Structure

### Core Components

#### Controllers

The Controllers in this Car Rental system handle HTTP requests and orchestrate the flow
between the frontend UI and backend services.

- `CarController`: Handles vehicle operations
- `ReservationController`: Manages booking flows
- `UserController`: User management endpoints

#### Services

Services encapsulate the core business logic and rules, acting as the intermediary
between controllers and repositories while enforcing domain-specific operations and validations.

- `CarService`: Vehicle inventory and availability logic
- `ReservationService`: Booking management and validation
- `UserService`: User authentication and profile management

#### Repositories

Repositories provide a clean abstraction layer for database operations, handling
all CRUD (Create, Read, Update, Delete) operations for their respective entities.

- `CarRepository`: Vehicle data access
- `ReservationRepository`: Booking records management
- `UserRepository`: User data persistence
- `CarTypeRepository`: Vehicle categories management

#### DTOs

DTOs serve as lightweight data carriers that safely transfer information between the application
layers while hiding internal entity complexity.

- `CarDTO`: Vehicle data transfer
- `ReservationDTO`: Booking information transfer
- `UserDTO`: User data transfer

#### Entities

Entities are the core domain objects that map directly to database tables and represent
the fundamental data structures of the car rental system.

- `Car`: Vehicle representation
- `CarType`: Vehicle category definition
- `User`: User account details
- `Reservation`: Booking records

### Testing

Comprehensive integration tests covering:

- Multiple reservation scenarios
- Overlapping booking prevention
- Multi-car type availability
- Edge case handling

## Class Architecture

```mermaid
classDiagram
%% Core Domain Entities
class Car {
-Integer carId
-CarStatus status
-CarType type
}
class CarType {
-VehicleType type
-Integer typeId
}
class Reservation {
-Integer reservationId
-LocalDate startDate
-LocalDate endDate
-ReservationStatus status
}
class User {
-Integer userId
-String username
-String password
}

    %% DTOs
    class CarDTO {
        -Integer carId
        -CarStatus status
        -VehicleType type
    }
    class ReservationDTO {
        -LocalDate startDate
        -LocalDate endDate
        -CarDTO car
        -UserDTO user
    }
    class UserDTO {
        -Integer userId
        -String firstName
        -String lastName
    }

    %% Services
    class CarService {
        +getCar(Integer) CarDTO
        +updateCarStatus(Integer, Status) CarDTO
    }
    class ReservationService {
        +createReservation(ReservationDTO) ReservationDTO
        +getReservation(Integer) ReservationDTO
    }
    class UserService {
        +getUser(Integer) UserDTO
        +createUser(UserDTO) UserDTO
    }

    %% Controllers
    class CarController {
        +getCar(Integer) ResponseEntity
        +updateCar(CarDTO) ResponseEntity
    }
    class ReservationController {
        +createReservation(ReservationDTO) ResponseEntity
        +getReservation(Integer) ResponseEntity
    }
    class UserController {
        +getUser(Integer) ResponseEntity
        +createUser(UserDTO) ResponseEntity
    }

    %% Factory
    class CarFactory {
        +createCar(CarDTO) Car
        +createCarDTO(Car) CarDTO
    }

    %% Relationships
    Car --> CarType
    Reservation --> Car
    Reservation --> User

    CarController --> CarService
    ReservationController --> ReservationService
    UserController --> UserService

    CarService --> CarFactory
    CarService --> CarDTO
    ReservationService --> ReservationDTO
    UserService --> UserDTO

    CarFactory ..> Car : creates
    CarFactory ..> CarDTO : creates

    CarDTO ..> Car : transforms
    ReservationDTO ..> Reservation : transforms
    UserDTO ..> User : transforms

```

## Dependencies

```

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.vaadin</groupId>
        <artifactId>vaadin-spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Features

- Car reservation system
- Multiple vehicle type support
- User management
- Booking validation
- Real-time availability tracking
- H2 in-memory database
- Vaadin-based UI

## Getting Started

- Clone the repository
- Run `mvn clean install`
- Start the application with `mvn spring-boot:run`
- Access the H2 console at `/h2-console`
- Access the application at `localhost:8080`
