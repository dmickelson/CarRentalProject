# Car Rental Project

I have created a car rental project using Java. I need to design and implement a simulated Car Rental system using object-oriented principles.

## Requirements

1. The system should allow reservation of a car of a given type at a desired date and time for a
   given number of days.
2. There are 3 types of cars (sedan, SUV and van).
3. The number of cars of each type is limited.
4. Use unit tests to prove the system satisfies the requirements.
5. Please be prepared to discuss the design and implementation during the interview.

## Potential Directory Structure

```
/src
  /main                         # Main application source code
    /java                       # Java source code
      /com/company/carrental            # Base package for your project
        /config                 # Configuration classes for Spring Security and other settings
          SecurityConfig.java   # Configures Spring Security (e.g., roles, authentication)
          AppConfig.java        # General application configuration, such as beans
        /controller             # MVC controllers handle HTTP requests
          CarController.java    # Controller for car-related operations (e.g., car reservation, car availability)
          ReservationController.java  # Controller for reservation-related operations (e.g., booking, viewing reservations)
          UserController.java   # Controller for user-related actions (e.g., registration, login)
        /dto                    # Data Transfer Objects (DTOs) for transferring data between layers
          CarDTO.java           # DTO for cars (fields like carName, carType, isAvailable)
          ReservationDTO.java   # DTO for reservations (fields like reservationId, userId, carId, startDate)
          UserDTO.java          # DTO for user data (fields like username, email)
        /entity                 # JPA entities that represent tables in the database
          Car.java              # Entity class representing the 'Car' database table
          CarType.java          # Entity class representing the 'CarType' database table
          Reservation.java      # Entity class representing the 'Reservation' database table
          User.java             # Entity class representing the 'User' database table
        /repository             # Spring Data JPA repositories for database interactions
          CarRepository.java    # Repository for accessing 'Car' data (CRUD operations)
          ReservationRepository.java  # Repository for accessing 'Reservation' data
          UserRepository.java   # Repository for accessing 'User' data
        /service                # Service layer for business logic
          CarService.java       # Business logic related to cars (e.g., availability, reservations)
          ReservationService.java  # Business logic related to reservations
          UserService.java      # Business logic related to user management (e.g., login, registration)
        /util                   # Utility classes for reusable methods or constants
          CustomException.java  # Custom exceptions for error handling
          DateUtils.java        # Utility class for date-related operations (e.g., date parsing, formatting)
        /view                   # Vaadin views (UI components)
          MainView.java         # Main Vaadin view for the application (home page, navigation)
          ReservationView.java  # Vaadin view to display and manage reservations
          LoginView.java        # Vaadin view for user login
          CarReservationForm.java # Form for reserving a car via Vaadin components (inputs, buttons)
    /resources                  # Resources like static files, templates, and configuration
      /static                   # Directory for static resources (images, CSS, JavaScript)
      /templates                # Thymeleaf templates (if applicable)
      application.properties    # Spring Boot configuration (e.g., database settings, security options)
      data.sql                  # Optional SQL file to initialize the in-memory H2 database with sample data
    /test                       # Test directory for unit and integration tests
      /java
        /com/company/carrental
          /controller           # Unit tests for controllers
            CarControllerTest.java      # Unit test for CarController
            ReservationControllerTest.java  # Unit test for ReservationController
          /service              # Unit tests for services
            CarServiceTest.java # Unit test for CarService
            ReservationServiceTest.java  # Unit test for ReservationService

```

### Explanation of Key Components:

1. Package Organization

- config: Configuration files, such as Spring Security configurations and any custom settings.
- controller: Spring MVC controllers handle HTTP requests and map them to services and views.
- dto: Data Transfer Objects (DTOs) are used to pass data between layers (e.g., from UI to service or from service to the UI). This helps in keeping your entities separate from the request/response models.
- entity: JPA entity classes that represent the data structure stored in the database.
- repository: JPA repositories for handling database operations. These should extend JpaRepository or CrudRepository for common database operations.
- service: This is the business logic layer. Each service handles a specific domain (e.g., car reservations, user management).
- util: Utility classes for common operations (e.g., date formatting, exception handling).
- view: Vaadin UI classes for building the user interface.

2. Vaadin Flow UI:

- MainView.java: This is the main layout or home page of the application, which will likely have navigation to other views.
- ReservationView.java: The page where users can see their current reservations.
- LoginView.java: A dedicated login page using Vaadin components, interacting with Spring Security.
- CarReservationForm.java: A form for users to reserve cars, using Vaadin components for input fields and validation.

3. Unit Testing:

- Separate unit tests for each controller and service. These tests ensure that your business logic and endpoints work as expected.
- Optionally, add integration tests to validate how different layers interact with each other (controllers, services, repositories).

4. Resources:

- static: This is where you can store static resources like CSS files or images.
- templates: If you decide to use Thymeleaf for server-side rendering, these templates will go here.
- application.properties: Your Spring Boot configuration (e.g., DB settings, security settings).

5. Database Initialization:

- data.sql: SQL script to initialize the H2 in-memory database with car types, cars, users, and sample reservations. This can be loaded automatically by Spring Boot on startup.

## Data Model

### Entities:

- **CarType**: `car_type_id`, `name` (Sedan, SUV, Van)
- **Car**: `car_id`, `car_type_id` (FK), `status` (available, reserved)
- **User**: `user_id`, `first_name`, `last_name`, `username`, `password`
- **Reservation**: `reservation_id`, `user_id` (FK), `car_id` (FK), `start_date`, `end_date`, `status` (active, cancelled, completed)

### Relationships:

- Each car belongs to a CarTy1pe.
- A `User` can make multiple reservations.
- A `Reservation` references a specific `Car` and the `User` who made it.

## Initialization and Setup

- It is important that we have a way to initialize the system with the number of cars of each type.
- The database we are using is H2 in memory dataabase, so the initialization should be done in the code in the initilization script.
- Initiliazation should include defaul data for:
  - CarType: The types of cars (sedan, SUV and van), so type id and type name.
  - Car: The cars of each type that are available, so car id, type id, and avaiability
  - User: User table to store the users information such as user Id, first name, lastname, username and password.
  - Reservation: Reservation table to store the reservations information such as reservation id, user id, car id, start date and time, end date and time.

## Sytsetm and architecture

### Technologies

- Java
- Java spring
- Spring boot
- Spring boot web
- Spring ups with h2
- Vaadin Java front end framework
- Simple spring security for an admin and user
- H2 will acta as the data store for user configuration, customers reservations and current fleet of vehicles

### Architecture

- SOLID architecture
- MVC systems architecture
- Use of software design patterns

## Backend Implementation

- Service Layer: Implement a service layer to encapsulate business logic. For example, have a ReservationService that handles creating reservations, checking availability, and ensuring the number of cars available is updated.
- Exception Handling: Add custom exception handling for cases like:
  - No cars available for the selected type and date range.
  - Invalid login credentials.
- Validation: Use annotations (e.g., @Valid) to validate user input, such as reservation dates (start date cannot be in the past, end date must be after start date).

## User experience walk through

- User logs into simple login UI this is a full page with a logo along the top and username and password
- user logs in
- User is shown their current reservations
- The front end application consists of
- Top logo which will be an image and a title
- Nav bar containing - home which is users current reservations, Make Reservation- which will let the user reserve a car
- User will click on the top nav to Make a Reservation
-

## Unit Testing

- Unit testing is done using Junit
- CarServiceTest: Test fetching available cars, adding/removing cars, and updating availability.
- ReservationServiceTest: Test reservation creation (with valid and invalid inputs), checking availability, and handling cancellations.
- UserServiceTest: Test user creation, login functionality, and role assignment.
- Controller Tests: Use MockMvc to test your Spring controllers.

## Readme.md

- The Readnme.md will act as a presentation, walk in g logically through the application, clearly explaining the approaches and techniques and technologies used
- It will also include a walk through of the application
- It wil include links to the github repo and the live application and database

H2 Console URL = /h2-console

Include the details of the application configuration and what those settings mean

```
vaadin.launch-browser=true
spring.application.name=carrental

spring.datasource.url=jdbc:h2:mem:carrental
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver

spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

## The Approach To Building the Application

Start with the core components of our application:

1. Entity Classes: Let's create our entity classes first, as they form the foundation of our data model. We'll start with Car.java, CarType.java, User.java, and Reservation.java in the /src/main/java/com/carrental/entity directory.
2. Repository Interfaces: Next, we'll create the repository interfaces for each entity in the /src/main/java/com/carrental/repository directory. This will allow us to perform database operations.
3. Service Layer: We'll implement the service layer classes in the /src/main/java/com/carrental/service directory, starting with CarService.java and ReservationService.java. These will contain our core business logic.
4. Data Transfer Objects (DTOs): Create the DTO classes in the /src/main/java/com/carrental/dto directory to facilitate data transfer between layers.
5. Controller Classes: Implement the controller classes in the /src/main/java/com/carrental/controller directory to handle HTTP requests and responses.
6. Configuration: Set up the necessary configuration classes in the /src/main/java/com/carrental/config directory, including SecurityConfig.java for Spring Security setup.
7. Database Initialization: Create the data.sql file in the /src/main/resources directory to initialize the H2 database with sample data.
8. Application Properties: Configure the application.properties file in the /src/main/resources directory with necessary settings for the H2 database and other configurations.
9. Vaadin Views: Start creating the Vaadin UI components in the /src/main/java/com/carrental/view directory, beginning with MainView.java and LoginView.java.
10. Unit Tests: As we develop each component, we'll create corresponding unit tests in the /src/test/java/com/carrental directory.

By following this order, we'll build our application from the ground up, ensuring that each layer is properly implemented before moving on to the next. This approach allows us to test and verify each component as we go, making it easier to identify and fix any issues early in the development process.

## Potential next steps

- Implement Cloud deployment
- Create Vue or React front end for more modern user architecture
- Building for the future If you eventually want to move away from Vaadin or include other frameworks (like Vue or React), Spring Web allows you to serve a REST API to the client-side framework. You can handle reservation requests, car updates, and user management via the API while leaving UI development to a more specialized frontend tool.

## Links

- https://start.spring.io/
