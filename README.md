# SpringApplication

A small Spring Boot REST application that demonstrates a Spring-managed bean
and constructor-based dependency injection.

## Requirements

- Java 17 or later
- Maven 3.6.3 or later

## Run the application

From this directory, run:

```sh
mvn spring-boot:run
```

Then open <http://localhost:8080/api/greeting>. It returns:

```json
{"message":"Hello, World!"}
```

Pass a name with the `name` query parameter, for example
<http://localhost:8080/api/greeting?name=Spring>.

## What to notice

- `GreetingService` is registered as a Spring bean by `@Service`.
- `GreetingController` receives that bean through its constructor; Spring
  creates and wires both objects.
- `@SpringBootApplication` starts Spring Boot and scans this package for
  components.

Run the tests with:

```sh
mvn test
```
